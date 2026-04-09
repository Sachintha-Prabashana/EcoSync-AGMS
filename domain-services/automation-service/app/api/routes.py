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
    Schema for incoming sensor telemetry data.
    """
    deviceId: str
    zoneId: str
    temperature: float
    humidity: float

@router.get("/health")
async def health_check():
    """
    Service health check endpoint.
    """
    return {"status": "UP", "service": "Automation Service"}

@router.post("/process")
async def process_data(request: AutomationRequest, db: Session = Depends(get_db)):
    """
    Processes sensor data, evaluates automation rules, and persists the decision.
    """
    logger.info(f"Processing telemetry from device {request.deviceId} in zone {request.zoneId}")
    
    # 1. Fetch zone thresholds from Zone Service
    zone_limits = await get_zone_limits(request.zoneId)
    
    if not zone_limits:
        logger.error(f"Could not retrieve limits for zone {request.zoneId}")
        raise HTTPException(
            status_code=404, 
            detail=f"Zone limits not found for zoneId: {request.zoneId}"
        )
    
    # 2. Process rules using automation service
    decision = evaluate_automation(request.temperature, zone_limits)
    
    # 3. Save final decision to the database
    try:
        new_log = AutomationLog(
            deviceId=request.deviceId,
            zoneId=request.zoneId,
            temperature=request.temperature,
            humidity=request.humidity,
            decision=decision
        )
        db.add(new_log)
        db.commit()
        db.refresh(new_log)
        
        logger.info(f"Automation decision '{decision}' saved with log ID {new_log.id}")
        
        # 4. Return the result
        return {
            "status": "success",
            "log_id": new_log.id,
            "decision": decision,
            "metadata": {
                "deviceId": request.deviceId,
                "zoneId": request.zoneId,
                "timestamp": new_log.timestamp
            }
        }
    except Exception as e:
        db.rollback()
        logger.error(f"Failed to save automation log: {e}")
        raise HTTPException(
            status_code=500,
            detail="Internal server error while saving automation decision"
        )