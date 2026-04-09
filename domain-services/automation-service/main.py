from dotenv import load_dotenv

# Load environment variables from .env file FIRST
load_dotenv()

import uvicorn
import os

from fastapi import FastAPI
from contextlib import asynccontextmanager
from app.core.config import init_eureka, stop_eureka
from app.core.database import init_db
from app.api.routes import router as api_router

# Manage Eureka and DB lifecycle using lifespan
@asynccontextmanager
async def lifespan(app: FastAPI):
    # Initialize database tables
    init_db()
    
    # When run startup register to eureka
    await init_eureka()
    yield

    # Shutdown - when service is stop remove from eureka
    await stop_eureka()
app = FastAPI(title="Automation Service", lifespan=lifespan)

# we can use like (http://localhost:8083/api/v1/automation/health)
app.include_router(api_router, prefix="/api/v1/automation")

if __name__ == "__main__":
    port = int(os.getenv("PORT", 8083))
    uvicorn.run("main:app", host="0.0.0.0", port=port, reload=True)