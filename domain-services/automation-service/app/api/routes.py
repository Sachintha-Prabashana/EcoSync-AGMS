from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from pydantic import BaseModel
from app.core.database import get_db
from app.services.automation_service import get_zone_limits, evaluate_automation
from app.models.automation_log import AutomationLog
import logging

logger = logging.getLogger(__name__)

router = APIRouter()

class AutomationRequest(BaseModel):
    """
    Schema for a single sensor reading.
    """
    deviceId: str
    zoneId: str
    temperature: float
    humidity: float

class BatchAutomationRequest(BaseModel):
    """
    Schema for a batch of sensor readings.
    """
    timestamp: str
    readings: list[AutomationRequest]

@router.get("/health")
async def health_check():
    """
    Service health check endpoint.
    """
    return {"status": "UP", "service": "Automation Service"}

@router.post("/process")
async def process_batch(request: BatchAutomationRequest, db: Session = Depends(get_db)):
    """
    Processes a batch of sensor data, evaluates automation rules for each, and persists decisions.
    """
    logger.info(f"Processing batch of {len(request.readings)} readings received at {request.timestamp}")
    
    results = []
    
    for reading in request.readings:
        try:
            # 1. Fetch zone thresholds from Zone Service
            zone_limits = await get_zone_limits(reading.zoneId)
            
            if not zone_limits:
                logger.error(f"Could not retrieve limits for zone {reading.zoneId}")
                results.append({"deviceId": reading.deviceId, "status": "error", "detail": "Zone limits not found"})
                continue
            
            # 2. Process rules using automation service
            decision = evaluate_automation(reading.temperature, zone_limits)
            
            # 3. Save final decision to the database
            new_log = AutomationLog(
                deviceId=reading.deviceId,
                zoneId=reading.zoneId,
                temperature=reading.temperature,
                humidity=reading.humidity,
                decision=decision
            )
            db.add(new_log)
            # Commit after each success to ensure data is saved
            db.commit()
            db.refresh(new_log)
            
            logger.info(f"Decision '{decision}' saved for device {reading.deviceId}")
            results.append({"deviceId": reading.deviceId, "status": "success", "decision": decision})

        except Exception as e:
            db.rollback()
            logger.error(f"Failed to process reading for device {reading.deviceId}: {e}")
            results.append({"deviceId": reading.deviceId, "status": "error", "detail": str(e)})

    return {
        "status": "batch_completed",
        "timestamp": request.timestamp,
        "processed_count": len(request.readings),
        "results": results
    }