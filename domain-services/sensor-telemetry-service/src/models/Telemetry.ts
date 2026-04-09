import mongoose, { Schema, Document } from 'mongoose';

export interface ITelemetry extends Document {
  deviceId: string;
  name: string;
  zoneId: string;
  temperature: number;
  humidity: number;
  timestamp: Date;
}

const TelemetrySchema: Schema = new Schema({
  deviceId: { type: String, required: true },
  name: { type: String, required: true },
  zoneId: { type: String, required: true },
  temperature: { type: Number, required: true },
  humidity: { type: Number, required: true },
  timestamp: { type: Date, default: Date.now }
});

// Index on timestamp for fast "latest" queries
TelemetrySchema.index({ timestamp: -1 });

export default mongoose.model<ITelemetry>('Telemetry', TelemetrySchema);
