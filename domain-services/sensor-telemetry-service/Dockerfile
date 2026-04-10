# Stage 1: Build
FROM node:20-alpine AS build
WORKDIR /app
RUN npm install -g pnpm
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
# If there's a build step for TS:
# RUN pnpm build

# Stage 2: Runtime
FROM node:20-alpine
WORKDIR /app
RUN npm install -g pnpm
COPY --from=build /app/package.json /app/pnpm-lock.yaml ./
RUN pnpm install --prod --frozen-lockfile
COPY --from=build /app/src ./src
COPY --from=build /app/tsconfig.json ./
# Expose the port
EXPOSE 8082
CMD ["npx", "tsx", "src/index.ts"]
