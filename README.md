# open-saml-3

## Gerar chave privada e certificado

### Gerar a chave privada (em PEM) e o certificado (self signed)
```
openssl req -x509 -newkey rsa:4096 -keyout private_key.pem -out certificate.pem -days 365 -nodes
```

### Converter o PEM em DER (para ser lido pelo java)
```
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt
```