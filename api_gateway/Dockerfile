FROM node:20
WORKDIR /app
COPY package*.json ./
RUN npm install
CMD ["npx", "nodemon", "src/index.ts"]
EXPOSE 8080
