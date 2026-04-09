import httpx
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

CONFIG_SERVER_URL = "http://localhost:8888/automation-service/default"

def fetch_remote_config():
    """
    Fetches configuration from Spring Cloud Config Server and flattens it.
    Returns a dictionary of configurations including database and service URLs.
    """
    try:
        logger.info(f"Fetching configuration from {CONFIG_SERVER_URL}")
        with httpx.Client(timeout=10.0) as client:
            response = client.get(CONFIG_SERVER_URL)
            response.raise_for_status()
            data = response.json()
            
            # Flatten property sources
            # Spring Cloud Config returns property sources in order of priority (highest first)
            # To flatten: we iterate in reverse so that items earlier in the list (higher priority) 
            # correctly overwrite items later in the list.
            flattened_config = {}
            property_sources = data.get("propertySources", [])
            
            for source in reversed(property_sources):
                config_source = source.get("source", {})
                flattened_config.update(config_source)
            
            # Extract specific requested configurations
            # Using common Spring Boot / Spring Cloud naming conventions
            database_url = flattened_config.get("spring.datasource.url")
            eureka_server = flattened_config.get("eureka.client.serviceUrl.defaultZone")
            
            # Construct a clean config dictionary
            configs = {
                "database_url": database_url,
                "eureka_server": eureka_server,
                "all_configs": flattened_config
            }
            
            # Add any other service-specific URLs if they exist in a standard pattern
            # e.g., mapping keys that end with .url or contain serviceUrl
            service_urls = {k: v for k, v in flattened_config.items() if "service" in k.lower() and "url" in k.lower()}
            configs["service_urls"] = service_urls
            
            logger.info("Successfully fetched and flattened remote configuration")
            return configs
            
    except httpx.HTTPError as e:
        logger.error(f"HTTP error occurred while fetching config: {e}")
    except Exception as e:
        logger.error(f"Unexpected error while fetching config: {e}")
    
    return {}

if __name__ == "__main__":
    # Test execution
    config = fetch_remote_config()
    if config:
        print("--- Remote Configuration Fetched ---")
        print(f"Database URL: {config.get('database_url')}")
        print(f"Eureka Server: {config.get('eureka_server')}")
        print(f"Service URLs: {config.get('service_urls')}")
    else:
        print("Failed to fetch configuration.")
