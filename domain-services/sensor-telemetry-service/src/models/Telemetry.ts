import mongoose, { Schema, Document } from 'mongoose';

export interface ITelemetry extends Document {
  deviceId: string;
  name: string;
  zoneId: string;
  data: any;
  timestamp: Date;
}

const TelemetrySchema: Schema = new Schema({
  deviceId: { type: String, required: true },
  name: { type: String, required: true },
  zoneId: { type: String, required: true },
  data: { type: Schema.Types.Mixed, required: true },
  timestamp: { type: Date, default: Date.now }
});

// We can index on timestamp for fast "latest" queries
TelemetrySchema.index({ timestamp: -1 });

export default mongoose.model<ITelemetry>('Telemetry', TelemetrySchema);
