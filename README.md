# open-saml-3

## Gerar chave privada, publica e certificado

### Gerar o PEM da chave privada
```
openssl genrsa -out private_key.pem 2048
```

### Converter o PEM em DER (para ser lido pelo java)
```
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt
```

### Gerar a chave publica a partir do PEM
```
openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der
```