# Etapa 1: Build do Frontend
FROM node:18 AS build

# Definir o diretório de trabalho
WORKDIR /app

# Copiar package.json e package-lock.json
COPY eccomercefront/package*.json ./

# Instalar dependências
RUN npm install

# Copiar o restante do código
COPY eccomercefront/ ./

# Build da aplicação
RUN npm run build

# Etapa 2: Servir com Nginx
FROM nginx:alpine

# Copiar a configuração personalizada do Nginx
COPY eccomercefront/nginx.conf /etc/nginx/conf.d/default.conf

# Copiar os arquivos buildados para o diretório padrão do Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Expor a porta 80
EXPOSE 80

# Comando para executar o Nginx
CMD ["nginx", "-g", "daemon off;"]
