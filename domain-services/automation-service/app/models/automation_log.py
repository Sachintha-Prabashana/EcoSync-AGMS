import uuid
from sqlalchemy import Column, String, Float, DateTime
from sqlalchemy.dialects.mysql import CHAR
from sqlalchemy.sql import func
from app.core.database import Base

class AutomationLog(Base):
    """
    SQLAlchemy model to store automation decisions and environment telemetry.
    """
    __tablename__ = "automation_logs"

    # id (UUID): Stored as CHAR(36) for MySQL compatibility
    id = Column(CHAR(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    
    # deviceId and zoneId fields as requested
    deviceId = Column(String(100), nullable=False, index=True)
    zoneId = Column(String(100), nullable=False, index=True)
    
    # Telemetry data
    temperature = Column(Float, nullable=False)
    humidity = Column(Float, nullable=False)
    
    # Automation decision (e.g., COOLING_ON, HEATING_ON, VENTILATION_OFF)
    decision = Column(String(100), nullable=False)
    
    # Automatically set timestamp on creation
    timestamp = Column(DateTime(timezone=True), server_default=func.now())

    def __repr__(self):
        return (f"<AutomationLog(id='{self.id}', device='{self.deviceId}', "
                f"decision='{self.decision}', timestamp='{self.timestamp}')>")
