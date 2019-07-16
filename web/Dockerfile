FROM node:11.13.0-alpine

# Create app directory
WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

EXPOSE ${NUXT_PORT}

CMD [ "npm", "start" ]