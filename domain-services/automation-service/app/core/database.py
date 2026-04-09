from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from app.core.remote_config import fetch_remote_config
import logging
import os

logger = logging.getLogger(__name__)

def get_database_url():
    """
    Retrieves the database URL from remote config and converts it to SQLAlchemy format if needed.
    """
    config = fetch_remote_config()
    db_url = config.get("database_url")
    
    if not db_url:
        # Check for multiple possible environment variables
        db_url = os.getenv("MYSQL_URL") or os.getenv("DATABASE_URL")
        
        if not db_url:
            # Default fallback for development
            db_url = "mysql+mysqlconnector://root:password@localhost:3306/ecosync_automation"
            logger.warning("Database URL not found in remote config or .env, using default fallback")
    
    # Convert JDBC URL to SQLAlchemy URL if necessary
    if db_url.startswith("jdbc:"):
        logger.info("Converting JDBC URL to SQLAlchemy compatible format")
        db_url = db_url.replace("jdbc:", "", 1)
        if db_url.startswith("mysql:"):
            db_url = db_url.replace("mysql:", "mysql+mysqlconnector:", 1)
            
    return db_url

DATABASE_URL = get_database_url()
logger.info(f"Connecting to database at: {DATABASE_URL}")

# Create SQLAlchemy engine
# pool_pre_ping helps verify connections before using them
engine = create_engine(
    DATABASE_URL, 
    pool_pre_ping=True
)

# Session factory
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Base class for models
Base = declarative_base()

def init_db():
    """
    Initializes the database by creating tables.
    """
    try:
        Base.metadata.create_all(bind=engine)
        logger.info("Database tables initialized successfully")
    except Exception as e:
        logger.error(f"Failed to initialize database: {e}")

def get_db():
    """
    Dependency to get a database session.
    """
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
