# Sistema de Gerenciamento de Menus para Restaurantes

Este é um sistema completo para restaurantes que permite aos clientes visualizar os menus disponíveis, realizar pedidos diretamente pelo sistema e acompanhar o status do pedido.  
Conta com autenticação via JWT, comunicação segura com OpenSSL, e uma arquitetura robusta baseada em Spring Boot, além de suporte a Docker Compose para fácil execução.

---
## Funcionalidades

- Autenticação e autorização com JWT
- Criptografia com OpenSSL (chaves RSA)
- Cadastro de produtos, menus e categorias
- Realização e acompanhamento de pedidos
- Diferentes perfis de usuário: `ADMIN`, `CLIENTE(BASIC)`
- Testes com JUnit e Mockito
- Ambiente pronto para rodar com Docker Compose

---

## Tecnologias utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **Maven**
- **JWT (JSON Web Tokens)**
- **OpenSSL**
- **JUnit**
- **Mockito**
- **MYSQL**
- **Docker Compose**

---

## Modo de iniciar o projeto localmente.

### 1. Clone o repositório

### 2. Gere as chaves (se ainda não tiver)
#### caso use o Linux ou Mac,pode saltar essa etapa e ir direito para gerar chaves
#### caso esteja usando o windows, baixe o OpenSSL
#### https://slproweb.com/products/Win32OpenSSL.html
- obs: baixe a versao Light dependendo da tua arquitectura (64/32)
#### Apois baixar o OpenSSL, faca a instalacao(duplo click)
- obs: Apois a instalacao bem sucedida, vai ate a pasta
#### C:\Program Files\OpenSSL-Win64\bin
#### Copia o caminho, e cria uma variavel de ambiente na variavel do sistema
- path: C:\Program Files\OpenSSL-Win64\bin
- No cmd ou powershell execute
>$  openssl --version
- deve retornar a versao do openssl
## Gerando as chaves
- gerar chave privada de 4.096 bits, o tamanho pode ser alterado por vc
>$ openssl genrsa -out private.key
- Dada uma chave privada, você pode derivar sua chave pública e enviá-la para públic.key
>$ openssl rsa -in private.key -pubout -out public.key
- As chaves são carregadas pelo backend, deves configurar o:
> propertie
>
> jwt.public.key=classpath:public.key
>
> jwt.private.key=classpath:private.key

---

## 🐳 Iniciando o Mysql com Docker Compose
### 1.Precisa ter o Docker instalado na sua maquina
### 2. Navegue na pasta Docker(na raiz do projecto) e execute
> $ docker compose up
### 3.Caso não queira usar o docker, pode usar o Mysql da sua maquina
obs: Não recomendo