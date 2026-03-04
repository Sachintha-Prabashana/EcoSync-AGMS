from fastapi import APIRouter

router = APIRouter()

@router.get("/health")
async def health_check():
    return {"status": "UP", "service": "Automation Service"}

@router.post("/process")
async def process_data():
    return {"message": "Data received and processing started"}