import mongoose from 'mongoose';

export const connectDB = async (mongoUri: string) => {
    try {
        console.log('Connecting to MongoDB...');
        await mongoose.connect(mongoUri);
        console.log('MongoDB Connected successfully.');
    } catch (error: any) {
        console.error('MongoDB Connection Error:', error.message);
        process.exit(1);
    }
};
