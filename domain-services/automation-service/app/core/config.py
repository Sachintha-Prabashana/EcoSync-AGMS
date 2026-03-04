import os
from py_eureka_client import eureka_client
from dotenv import load_dotenv

load_dotenv()

async def init_eureka():
    await eureka_client.init_async(
        eureka_server=os.getenv("EUREKA_SERVER"),
        app_name=os.getenv("APP_NAME", "automation-service"),
        instance_port=int(os.getenv("PORT", 8083)),
        instance_host="localhost"
    )
    print(f"{os.getenv('APP_NAME')} registered with Eureka")

async def stop_eureka():
    await eureka_client.stop_async()
    print("Eureka registration stopped")