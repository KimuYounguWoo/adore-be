FROM hashicorp/vault
COPY ./config /vault/config
COPY ./file /vault/file
COPY ./logs /vault/logs
EXPOSE 8200
CMD ["vault", "server", "-config=/vault/config"]