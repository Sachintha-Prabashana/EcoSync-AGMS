import httpx
import logging

logger = logging.getLogger(__name__)

# Zone Management Service base URL
ZONE_SERVICE_URL = "http://localhost:8081/api/zones"

async def get_zone_limits(zone_id: str):
    """
    Asynchronously fetches zone thresholds from the Zone Management Service.
    """
    try:
        logger.info(f"Fetching limits for zone {zone_id} from {ZONE_SERVICE_URL}")
        async with httpx.AsyncClient(timeout=5.0) as client:
            response = await client.get(f"{ZONE_SERVICE_URL}/{zone_id}")
            response.raise_for_status()
            return response.json()
    except httpx.HTTPError as e:
        logger.error(f"HTTP error connecting to Zone Management Service: {e}")
    except Exception as e:
        logger.error(f"Unexpected error fetching zone limits: {e}")
    return None

def evaluate_automation(temperature: float, limits: dict) -> str:
    """
    Compares current temperature against zone limits and returns an automation decision.
    
    Logic:
    - temperature > maxTemp -> 'COOLING_ON'
    - temperature < minTemp -> 'HEATING_ON'
    - otherwise -> 'STABLE'
    """
    if not limits:
        logger.warning("No limits provided, defaulting to STABLE")
        return "STABLE"

    min_temp = limits.get("minTemp")
    max_temp = limits.get("maxTemp")

    if min_temp is None or max_temp is None:
        logger.warning(f"Missing temperature thresholds in limits: {limits}")
        return "STABLE"

    if temperature > max_temp:
        logger.info(f"Temperature {temperature} exceeds max {max_temp}. Decision: COOLING_ON")
        return "COOLING_ON"
    elif temperature < min_temp:
        logger.info(f"Temperature {temperature} is below min {min_temp}. Decision: HEATING_ON")
        return "HEATING_ON"
    else:
        logger.info(f"Temperature {temperature} is within range [{min_temp}, {max_temp}]. Decision: STABLE")
        return "STABLE"
