# Contrato da API REST - Royal Enfield

Este documento especifica o contrato completo de integração com os endpoints REST da API Royal Enfield, contendo rotas, métodos HTTP, parâmetros, formatos de payload (JSON e Multipart), respostas de sucesso e respostas de erro.

---

## Informações Gerais
* **Base URL**: `http://localhost:8080`
* **API Prefix**: `/api/v1`
* **Arquivos Estáticos (Uploads)**: `http://localhost:8080/uploads/**`
* **Formato Padrão**: `application/json;charset=UTF-8`
* **Uploads de Mídia**: `multipart/form-data` (extensões aceitas: `.jpg`, `.jpeg`, `.png`, `.webp`)

---

## Padrão de Respostas de Erro (RFC 7807 - ProblemDetail)

Todas as respostas de erro seguem o padrão RFC 7807 (`application/problem+json`).

### 1. Erro de Recurso Não Encontrado (404 Not Found)
```json
{
  "type": "https://api.royalenfield.com/errors/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Motorcycle not found with ID: 73941604-b4ad-4049-a68e-a4117fe0dc14",
  "instance": "/api/v1/motorcycles/73941604-b4ad-4049-a68e-a4117fe0dc14",
  "timestamp": "2026-08-28T19:53:32.815Z"
}
```

### 2. Erro de Validação de Campos (400 Bad Request)
```json
{
  "type": "https://api.royalenfield.com/errors/validation",
  "title": "Validation Error",
  "status": 400,
  "detail": "Validation failed for one or more fields",
  "instance": "/api/v1/motorcycles",
  "timestamp": "2026-08-28T19:53:32.815Z",
  "invalidFields": {
    "modelName": "Model name is required",
    "startingPrice": "Starting price must be greater than zero",
    "engineCc": "Engine CC is required"
  }
}
```

### 3. Erro de Método Não Permitido (405 Method Not Allowed)
```json
{
  "type": "https://api.royalenfield.com/errors/method-not-allowed",
  "title": "Method Not Allowed",
  "status": 405,
  "detail": "Request method 'PUT' is not supported for this endpoint. Supported methods: [POST, GET]",
  "instance": "/api/v1/motorcycles/ad7f150a-516c-49d2-b72c-53d077045994/gallery",
  "timestamp": "2026-08-28T20:27:24.258Z"
}
```

### 4. Erro de Armazenamento / Tipo de Arquivo Inválido (400 Bad Request)
```json
{
  "type": "https://api.royalenfield.com/errors/file-storage",
  "title": "File Storage Error",
  "status": 400,
  "detail": "Invalid file type: .pdf. Allowed: [jpg, jpeg, png, webp]",
  "instance": "/api/v1/variants/ad7f150a-516c-49d2-b72c-53d077045994/image",
  "timestamp": "2026-08-28T19:53:32.815Z"
}
```

---

## 1. Módulo de Motocicletas (/api/v1/motorcycles)

### 1.1. Criar Motocicleta
* **Método**: `POST`
* **Rota**: `/api/v1/motorcycles`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "modelName": "Super Meteor 650",
  "family": "Cruiser",
  "engineCc": 648,
  "startingPrice": 33990.00,
  "description": "Cruiser premium com motor bicilíndrico paralelo de 648cc.",
  "active": true,
  "technicalSpec": {
    "powerHp": "47 hp @ 7250 rpm",
    "torqueNm": "52.3 Nm @ 5650 rpm",
    "weightKg": 241.00,
    "fuelCapacityL": 15.70,
    "seatHeightMm": 740,
    "transmission": "6 marchas com embreagem assistida e deslizante",
    "frontBrake": "Disco único de 320mm com ABS de canal duplo",
    "rearBrake": "Disco único de 300mm com ABS de canal duplo",
    "coolingSystem": "Ar e radiador de óleo"
  }
}
```
> **Nota**: O objeto `technicalSpec` é opcional na criação da moto.

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "ad7f150a-516c-49d2-b72c-53d077045994",
  "modelName": "Super Meteor 650",
  "family": "Cruiser",
  "engineCc": 648,
  "startingPrice": 33990.00,
  "description": "Cruiser premium com motor bicilíndrico paralelo de 648cc.",
  "active": true,
  "technicalSpec": {
    "id": "e4a2d890-1c23-4b56-7890-abcdef123456",
    "powerHp": "47 hp @ 7250 rpm",
    "torqueNm": "52.3 Nm @ 5650 rpm",
    "weightKg": 241.00,
    "fuelCapacityL": 15.70,
    "seatHeightMm": 740,
    "transmission": "6 marchas com embreagem assistida e deslizante",
    "frontBrake": "Disco único de 320mm com ABS de canal duplo",
    "rearBrake": "Disco único de 300mm com ABS de canal duplo",
    "coolingSystem": "Ar e radiador de óleo",
    "createdAt": "2026-08-28T19:54:16.511Z",
    "updatedAt": "2026-08-28T19:54:16.511Z"
  },
  "variants": [],
  "gallery": [],
  "createdAt": "2026-08-28T19:54:16.511Z",
  "updatedAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 1.2. Listar Motocicletas (Paginado com Filtros)
* **Método**: `GET`
* **Rota**: `/api/v1/motorcycles`
* **Query Parameters (Opcionais)**:
  * `family` (string): Filtra por família da moto (ex: `Cruiser`, `Classic`, `Roadster`, `Adventure`).
  * `active` (boolean): `true` ou `false`.
  * `query` (string): Busca textual no nome do modelo (ex: `meteor`, `hunter`).
  * `page` (int, default: `0`): Índice da página.
  * `size` (int, default: `10`): Quantidade de itens por página.
  * `sort` (string, default: `modelName,asc`): Campo e direção de ordenação.

#### Exemplo de Requisição:
`GET /api/v1/motorcycles?family=Cruiser&active=true&page=0&size=10`

#### Resposta de Sucesso (`200 OK`):
```json
{
  "content": [
    {
      "id": "ad7f150a-516c-49d2-b72c-53d077045994",
      "modelName": "Super Meteor 650",
      "family": "Cruiser",
      "engineCc": 648,
      "startingPrice": 33990.00,
      "description": "Cruiser premium com motor bicilíndrico paralelo de 648cc.",
      "active": true,
      "mainImageUrl": "/uploads/variants/celestial-red.webp",
      "variantCount": 3,
      "createdAt": "2026-08-28T19:54:16.511Z",
      "updatedAt": "2026-08-28T19:54:16.511Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### 1.3. Obter Detalhes da Motocicleta por ID
* **Método**: `GET`
* **Rota**: `/api/v1/motorcycles/{id}`

#### Resposta de Sucesso (`200 OK`):
Retorna a motocicleta com sua ficha técnica completa, array de variantes e array de imagens da galeria.
```json
{
  "id": "ad7f150a-516c-49d2-b72c-53d077045994",
  "modelName": "Super Meteor 650",
  "family": "Cruiser",
  "engineCc": 648,
  "startingPrice": 33990.00,
  "description": "Cruiser premium com motor bicilíndrico paralelo de 648cc.",
  "active": true,
  "technicalSpec": {
    "id": "e4a2d890-1c23-4b56-7890-abcdef123456",
    "powerHp": "47 hp @ 7250 rpm",
    "torqueNm": "52.3 Nm @ 5650 rpm",
    "weightKg": 241.00,
    "fuelCapacityL": 15.70,
    "seatHeightMm": 740,
    "transmission": "6 marchas com embreagem assistida e deslizante",
    "frontBrake": "Disco único de 320mm com ABS de canal duplo",
    "rearBrake": "Disco único de 300mm com ABS de canal duplo",
    "coolingSystem": "Ar e radiador de óleo",
    "createdAt": "2026-08-28T19:54:16.511Z",
    "updatedAt": "2026-08-28T19:54:16.511Z"
  },
  "variants": [
    {
      "id": "11111111-2222-3333-4444-555555555555",
      "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
      "variantName": "Astral",
      "colorName": "Astral Black",
      "hexColorCode": "#0A0A0A",
      "price": 33990.00,
      "imageUrl": "/uploads/variants/astral-black.webp",
      "includedAccessories": "Espelhos clássicos, rodas de liga leve",
      "active": true,
      "createdAt": "2026-08-28T19:54:16.511Z",
      "updatedAt": "2026-08-28T19:54:16.511Z"
    }
  ],
  "gallery": [
    {
      "id": "99999999-8888-7777-6666-555555555555",
      "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
      "imageUrl": "/uploads/gallery/front-view.jpg",
      "caption": "Vista Frontal",
      "displayOrder": 1,
      "createdAt": "2026-08-28T19:54:16.511Z"
    }
  ],
  "createdAt": "2026-08-28T19:54:16.511Z",
  "updatedAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 1.4. Atualizar Dados Básicos da Motocicleta
* **Método**: `PUT`
* **Rota**: `/api/v1/motorcycles/{id}`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "modelName": "Super Meteor 650 Twin",
  "family": "Cruiser",
  "engineCc": 648,
  "startingPrice": 34990.00,
  "description": "Cruiser topo de linha da Royal Enfield atualizada.",
  "active": true
}
```

#### Resposta de Sucesso (`200 OK`): Retorna o objeto `MotorcycleDetailResponse` atualizado.

---

### 1.5. Ativar / Desativar Motocicleta (Toggle Status)
* **Método**: `PATCH`
* **Rota**: `/api/v1/motorcycles/{id}/toggle-status`

#### Resposta de Sucesso (`200 OK`):
```json
{
  "id": "ad7f150a-516c-49d2-b72c-53d077045994",
  "modelName": "Super Meteor 650",
  "family": "Cruiser",
  "engineCc": 648,
  "startingPrice": 33990.00,
  "description": "Cruiser premium com motor bicilíndrico paralelo de 648cc.",
  "active": false,
  "mainImageUrl": null,
  "variantCount": 0,
  "createdAt": "2026-08-28T19:54:16.511Z",
  "updatedAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 1.6. Excluir Motocicleta
* **Método**: `DELETE`
* **Rota**: `/api/v1/motorcycles/{id}`

> **Efeito colateral**: Exclui em cascata a ficha técnica, todas as variantes, todos os itens da galeria e remove fisicamente todos os arquivos de imagem associados do diretório `./uploads/`.

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 2. Módulo de Ficha Técnica (/api/v1/motorcycles/{motorcycleId}/technical-spec)

### 2.1. Obter Ficha Técnica
* **Método**: `GET`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/technical-spec`

#### Resposta de Sucesso (`200 OK`):
```json
{
  "id": "e4a2d890-1c23-4b56-7890-abcdef123456",
  "powerHp": "47 hp @ 7250 rpm",
  "torqueNm": "52.3 Nm @ 5650 rpm",
  "weightKg": 241.00,
  "fuelCapacityL": 15.70,
  "seatHeightMm": 740,
  "transmission": "6 marchas com embreagem assistida e deslizante",
  "frontBrake": "Disco único de 320mm",
  "rearBrake": "Disco único de 300mm",
  "coolingSystem": "Ar e radiador de óleo",
  "createdAt": "2026-08-28T19:54:16.511Z",
  "updatedAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 2.2. Criar ou Atualizar Ficha Técnica (Upsert)
* **Método**: `PUT`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/technical-spec`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "powerHp": "47 hp @ 7250 rpm",
  "torqueNm": "52.3 Nm @ 5650 rpm",
  "weightKg": 241.00,
  "fuelCapacityL": 15.70,
  "seatHeightMm": 740,
  "transmission": "6 marchas",
  "frontBrake": "Disco único 320mm",
  "rearBrake": "Disco único 300mm",
  "coolingSystem": "Ar-Óleo"
}
```

#### Resposta de Sucesso (`200 OK`): Retorna `TechnicalSpecResponse`.

---

### 2.3. Remover Ficha Técnica
* **Método**: `DELETE`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/technical-spec`

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 3. Módulo de Variantes e Cores (/api/v1)

### 3.1. Criar Variante via JSON (Sem Upload Imediato)
* **Método**: `POST`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/variants`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "variantName": "Interstellar",
  "colorName": "Interstellar Grey",
  "hexColorCode": "#6E7072",
  "price": 34990.00,
  "imageUrl": null,
  "includedAccessories": "Pintura em dois tons, detalhes pretos",
  "active": true
}
```

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "c1f2e3d4-5678-90ab-cdef-1234567890ab",
  "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
  "variantName": "Interstellar",
  "colorName": "Interstellar Grey",
  "hexColorCode": "#6E7072",
  "price": 34990.00,
  "imageUrl": null,
  "includedAccessories": "Pintura em dois tons, detalhes pretos",
  "active": true,
  "createdAt": "2026-08-28T19:54:16.511Z",
  "updatedAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 3.2. Criar Variante com Upload de Imagem (multipart/form-data)
* **Método**: `POST`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/variants`
* **Headers**: `Content-Type: multipart/form-data`
* **Form Parts**:
  * `data` (application/json): Objeto JSON da variante (mesmo formato da seção 3.1).
  * `image` (file): Arquivo de imagem (`.png`, `.jpg`, `.jpeg`, `.webp`).

#### Resposta de Sucesso (`201 Created`): Retorna `MotorcycleVariantResponse` com o campo `imageUrl` preenchido com `/uploads/variants/{uuid}.ext`.

---

### 3.3. Listar Variantes de uma Moto
* **Método**: `GET`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/variants`
* **Query Parameters (Opcional)**:
  * `activeOnly` (boolean, default: `false`): Filtra apenas variantes ativas.

#### Resposta de Sucesso (`200 OK`): Retorna `List<MotorcycleVariantResponse>`.

---

### 3.4. Obter Variante por ID
* **Método**: `GET`
* **Rota**: `/api/v1/variants/{id}`

#### Resposta de Sucesso (`200 OK`): Retorna `MotorcycleVariantResponse`.

---

### 3.5. Atualizar Dados da Variante
* **Método**: `PUT`
* **Rota**: `/api/v1/variants/{id}`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "variantName": "Celestial Tourer",
  "colorName": "Celestial Blue",
  "hexColorCode": "#1A365D",
  "price": 36990.00,
  "imageUrl": null,
  "includedAccessories": "Para-brisa touring, encosto de garupa e banco luxo",
  "active": true
}
```

#### Resposta de Sucesso (`200 OK`): Retorna `MotorcycleVariantResponse`.

---

### 3.6. Fazer Upload/Substituição de Imagem da Variante
* **Método**: `POST` ou `PUT`
* **Rota**: `/api/v1/variants/{id}/image`
* **Headers**: `Content-Type: multipart/form-data`
* **Form Params**:
  * `file` (file): Arquivo da imagem.

> **Efeito colateral**: Se a variante já possuía uma imagem salva no disco local, o arquivo antigo é automaticamente excluído.

#### Resposta de Sucesso (`200 OK`): Retorna `MotorcycleVariantResponse` com a nova `imageUrl`.

---

### 3.7. Excluir Variante
* **Método**: `DELETE`
* **Rota**: `/api/v1/variants/{id}`

> **Efeito colateral**: Exclui o registro do banco e remove fisicamente o arquivo de imagem associado de `./uploads/variants/`.

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 4. Módulo de Galeria (/api/v1)

### 4.1. Adicionar Imagem na Galeria via Upload Físico
* **Método**: `POST`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/gallery`
* **Headers**: `Content-Type: multipart/form-data`
* **Form Params**:
  * `file` (file, obrigatório): Arquivo de imagem.
  * `caption` (string, opcional): Legenda da foto (ex: "Painel de Instrumentos Digital").
  * `displayOrder` (int, opcional, default: `0`): Ordem de exibição no carrossel.

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "77777777-6666-5555-4444-333333333333",
  "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
  "imageUrl": "/uploads/gallery/9b23c4a1-0e1f-4d62-9e8a-7c98b6543210.jpg",
  "caption": "Painel de Instrumentos Digital",
  "displayOrder": 1,
  "createdAt": "2026-08-28T19:54:16.511Z"
}
```

---

### 4.2. Adicionar Imagem na Galeria via URL Externa
* **Método**: `POST`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/gallery/url`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "imageUrl": "https://external-cdn.com/super-meteor-lifestyle.jpg",
  "caption": "Foto promocional de estrada",
  "displayOrder": 2
}
```

#### Resposta de Sucesso (`201 Created`): Retorna `MotorcycleGalleryResponse`.

---

### 4.3. Listar Galeria de uma Moto
* **Método**: `GET`
* **Rota**: `/api/v1/motorcycles/{motorcycleId}/gallery`

#### Resposta de Sucesso (`200 OK`):
Retorna a lista de itens da galeria ordenada ascendentemente por `displayOrder`.
```json
[
  {
    "id": "77777777-6666-5555-4444-333333333333",
    "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
    "imageUrl": "/uploads/gallery/9b23c4a1-0e1f-4d62-9e8a-7c98b6543210.jpg",
    "caption": "Painel de Instrumentos Digital",
    "displayOrder": 1,
    "createdAt": "2026-08-28T19:54:16.511Z"
  }
]
```

---

### 4.4. Atualizar Imagem ou Legenda da Galeria
* **Método**: `PUT`
* **Rota**: `/api/v1/gallery/{id}`
* **Headers**: `Content-Type: multipart/form-data`
* **Form Params (Opcionais)**:
  * `file` (file): Novo arquivo de imagem (se enviado, o arquivo anterior é removido do disco).
  * `caption` (string): Nova legenda.
  * `displayOrder` (int): Nova ordem de exibição.

#### Resposta de Sucesso (`200 OK`): Retorna `MotorcycleGalleryResponse` atualizado.

---

### 4.5. Remover Imagem da Galeria
* **Método**: `DELETE`
* **Rota**: `/api/v1/gallery/{id}`

> **Efeito colateral**: Se a imagem estiver armazenada na pasta local (`/uploads/gallery/...`), o arquivo físico é deletado do disco.

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 5. Módulo de Concessionárias (/api/v1/dealerships)

### 5.1. Criar Concessionária
* **Método**: `POST`
* **Rota**: `/api/v1/dealerships`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "name": "Royal Enfield Moema",
  "city": "Sao Paulo",
  "state": "SP",
  "address": "Av. Ibirapuera, 2907 - Moema",
  "phone": "(11) 5051-0000",
  "email": "moema@royalenfield.com.br"
}
```

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "11111111-2222-3333-4444-555555555555",
  "name": "Royal Enfield Moema",
  "city": "Sao Paulo",
  "state": "SP",
  "address": "Av. Ibirapuera, 2907 - Moema",
  "phone": "(11) 5051-0000",
  "email": "moema@royalenfield.com.br",
  "createdAt": "2026-08-31T19:30:00.000Z",
  "updatedAt": "2026-08-31T19:30:00.000Z"
}
```

---

### 5.2. Listar Concessionárias (Paginado com Filtros)
* **Método**: `GET`
* **Rota**: `/api/v1/dealerships`
* **Query Parameters (Opcionais)**:
  * `state` (string): Sigla do estado (ex: `SP`, `RJ`, `MG`).
  * `city` (string): Nome da cidade.
  * `query` (string): Busca textual no nome ou endereço.
  * `page` (int, default: `0`): Índice da página.
  * `size` (int, default: `10`): Quantidade de itens por página.
  * `sort` (string, default: `name,asc`): Campo e direção de ordenação.

#### Exemplo de Requisição:
`GET /api/v1/dealerships?state=SP&city=Sao Paulo`

#### Resposta de Sucesso (`200 OK`):
```json
{
  "content": [
    {
      "id": "11111111-2222-3333-4444-555555555555",
      "name": "Royal Enfield Moema",
      "city": "Sao Paulo",
      "state": "SP",
      "address": "Av. Ibirapuera, 2907 - Moema",
      "phone": "(11) 5051-0000",
      "email": "moema@royalenfield.com.br",
      "createdAt": "2026-08-31T19:30:00.000Z",
      "updatedAt": "2026-08-31T19:30:00.000Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "size": 10,
  "number": 0,
  "first": true,
  "numberOfElements": 1,
  "empty": false
}
```

---

### 5.3. Obter Concessionária por ID
* **Método**: `GET`
* **Rota**: `/api/v1/dealerships/{id}`

#### Resposta de Sucesso (`200 OK`): Retorna `DealershipResponse`.

---

### 5.4. Atualizar Concessionária
* **Método**: `PUT`
* **Rota**: `/api/v1/dealerships/{id}`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "name": "Royal Enfield Moema Premium",
  "city": "Sao Paulo",
  "state": "SP",
  "address": "Av. Ibirapuera, 2907 - Moema",
  "phone": "(11) 5051-9999",
  "email": "contato.moema@royalenfield.com.br"
}
```

#### Resposta de Sucesso (`200 OK`): Retorna `DealershipResponse` atualizado.

---

### 5.5. Excluir Concessionária
* **Método**: `DELETE`
* **Rota**: `/api/v1/dealerships/{id}`

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 6. Módulo de Test Rides (/api/v1/test-rides)

### 6.1. Solicitar Agendamento de Test Ride
* **Método**: `POST`
* **Rota**: `/api/v1/test-rides`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "customerName": "Carlos Silva",
  "customerEmail": "carlos.silva@email.com",
  "customerPhone": "(11) 98765-4321",
  "preferredDate": "2026-09-15T14:30:00Z",
  "motorcycleId": "ad7f150a-516c-49d2-b72c-53d077045994",
  "variantId": "11111111-2222-3333-4444-555555555555",
  "dealershipId": "11111111-2222-3333-4444-555555555555"
}
```
> **Nota**: `variantId` é opcional caso o cliente não tenha preferência por uma variante específica.

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "99999999-aaaa-bbbb-cccc-dddddddddddd",
  "customerName": "Carlos Silva",
  "customerEmail": "carlos.silva@email.com",
  "customerPhone": "(11) 98765-4321",
  "preferredDate": "2026-09-15T14:30:00Z",
  "status": "PENDING",
  "motorcycle": {
    "id": "ad7f150a-516c-49d2-b72c-53d077045994",
    "modelName": "Super Meteor 650",
    "family": "Cruiser",
    "engineCc": 648
  },
  "variant": {
    "id": "11111111-2222-3333-4444-555555555555",
    "variantName": "Astral",
    "colorName": "Astral Black",
    "imageUrl": "/uploads/variants/astral-black.webp"
  },
  "dealership": {
    "id": "11111111-2222-3333-4444-555555555555",
    "name": "Royal Enfield Moema",
    "city": "Sao Paulo",
    "state": "SP",
    "address": "Av. Ibirapuera, 2907 - Moema",
    "phone": "(11) 5051-0000",
    "email": "moema@royalenfield.com.br",
    "createdAt": "2026-08-31T19:30:00.000Z",
    "updatedAt": "2026-08-31T19:30:00.000Z"
  },
  "createdAt": "2026-08-31T19:30:00.000Z",
  "updatedAt": "2026-08-31T19:30:00.000Z"
}
```

---

### 6.2. Listar Test Rides (Paginado com Filtros)
* **Método**: `GET`
* **Rota**: `/api/v1/test-rides`
* **Query Parameters (Opcionais)**:
  * `dealershipId` (UUID): Filtra por concessionária.
  * `motorcycleId` (UUID): Filtra por motocicleta.
  * `status` (string): `PENDING`, `CONFIRMED`, `COMPLETED`, `CANCELLED`.
  * `customerEmail` (string): Filtra por e-mail do cliente.
  * `startDate` (ISO DateTime): Início da faixa de agendamento.
  * `endDate` (ISO DateTime): Fim da faixa de agendamento.
  * `page` (int, default: `0`): Índice da página.
  * `size` (int, default: `10`): Quantidade de itens por página.
  * `sort` (string, default: `preferredDate,asc`): Campo e direção de ordenação.

#### Exemplo de Requisição:
`GET /api/v1/test-rides?status=PENDING&page=0&size=10`

#### Resposta de Sucesso (`200 OK`): Retorna página de `TestRideResponse`.

---

### 6.3. Obter Test Ride por ID
* **Método**: `GET`
* **Rota**: `/api/v1/test-rides/{id}`

#### Resposta de Sucesso (`200 OK`): Retorna `TestRideResponse`.

---

### 6.4. Atualizar Status do Test Ride
* **Método**: `PATCH`
* **Rota**: `/api/v1/test-rides/{id}/status`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "status": "CONFIRMED"
}
```

#### Resposta de Sucesso (`200 OK`): Retorna `TestRideResponse` com o novo status.

---

### 6.5. Cancelar Test Ride
* **Método**: `PATCH`
* **Rota**: `/api/v1/test-rides/{id}/cancel`

#### Resposta de Sucesso (`200 OK`): Retorna `TestRideResponse` com status `CANCELLED`.

---

### 6.6. Excluir Test Ride
* **Método**: `DELETE`
* **Rota**: `/api/v1/test-rides/{id}`

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## 7. Módulo de Autenticação e 2FA (/api/v1/auth)

### 7.1. Registro Público de Usuário
* **Método**: `POST`
* **Rota**: `/api/v1/auth/register`
* **Headers**: `Content-Type: application/json`

> **Nota**: Novos cadastros recebem automaticamente o perfil `ROLE_VISITOR`. Um e-mail informativo é disparado de forma assíncrona para a caixa postal do Administrador.

#### Payload de Envio (Request Body):
```json
{
  "name": "Recrutador Tech",
  "email": "recrutador@empresa.com",
  "password": "senhaSegura123"
}
```

#### Resposta de Sucesso (`201 Created`):
```json
{
  "id": "33333333-4444-5555-6666-777777777777",
  "name": "Recrutador Tech",
  "email": "recrutador@empresa.com",
  "role": "ROLE_VISITOR",
  "lastLoginAt": null,
  "createdAt": "2026-09-04T08:50:00.000Z",
  "updatedAt": "2026-09-04T08:50:00.000Z"
}
```

---

### 7.2. Iniciar Login (Passo 1 do 2FA)
* **Método**: `POST`
* **Rota**: `/api/v1/auth/login`
* **Headers**: `Content-Type: application/json`

> **Nota**: Valida o e-mail e a senha do usuário. Se corretos, gera um código de 6 dígitos com validade de 10 minutos e envia para o e-mail cadastrado.

#### Payload de Envio (Request Body):
```json
{
  "email": "recrutador@empresa.com",
  "password": "senhaSegura123"
}
```

#### Resposta de Sucesso (`200 OK`):
```json
{
  "requires2FA": true,
  "email": "recrutador@empresa.com",
  "message": "Codigo de autenticacao enviado para o seu e-mail"
}
```

---

### 7.3. Confirmar Código 2FA e Obter Token JWT (Passo 2 do 2FA)
* **Método**: `POST`
* **Rota**: `/api/v1/auth/verify-2fa`
* **Headers**: `Content-Type: application/json`

#### Payload de Envio (Request Body):
```json
{
  "email": "recrutador@empresa.com",
  "code": "849201"
}
```

#### Resposta de Sucesso (`200 OK`):
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": "33333333-4444-5555-6666-777777777777",
    "name": "Recrutador Tech",
    "email": "recrutador@empresa.com",
    "role": "ROLE_VISITOR",
    "lastLoginAt": "2026-09-04T08:52:00.000Z",
    "createdAt": "2026-09-04T08:50:00.000Z",
    "updatedAt": "2026-09-04T08:52:00.000Z"
  }
}
```

---

### 7.4. Consultar Usuário Logado
* **Método**: `GET`
* **Rota**: `/api/v1/auth/me`
* **Headers**: `Authorization: Bearer <token>`

#### Resposta de Sucesso (`200 OK`): Retorna `UserResponse`.

---

## 8. Módulo de Gestão de Usuários (/api/v1/users)

> **Restrição de Acesso**: Todos os endpoints deste módulo exigem o perfil **`ROLE_ADMIN`**.

### 8.1. Listar Usuários (Paginado com Filtros)
* **Método**: `GET`
* **Rota**: `/api/v1/users`
* **Headers**: `Authorization: Bearer <admin_token>`
* **Query Parameters (Opcionais)**:
  * `neverAccessed` (boolean): `true` para filtrar apenas usuários que nunca realizaram login/2FA (`lastLoginAt` nulo).
  * `role` (string): `ROLE_VISITOR`, `ROLE_USER`, `ROLE_ADMIN`.
  * `query` (string): Busca textual por nome ou e-mail.
  * `page` (int, default: `0`): Índice da página.
  * `size` (int, default: `10`): Quantidade por página.
  * `sort` (string, default: `createdAt,desc`): Campo e direção de ordenação.

#### Resposta de Sucesso (`200 OK`): Retorna página de `UserResponse`.

---

### 8.2. Obter Usuário por ID
* **Método**: `GET`
* **Rota**: `/api/v1/users/{id}`
* **Headers**: `Authorization: Bearer <admin_token>`

#### Resposta de Sucesso (`200 OK`): Retorna `UserResponse`.

---

### 8.3. Alterar Papel (Role) do Usuário
* **Método**: `PATCH`
* **Rota**: `/api/v1/users/{id}/role`
* **Headers**: `Content-Type: application/json`, `Authorization: Bearer <admin_token>`

#### Payload de Envio (Request Body):
```json
{
  "role": "ROLE_USER"
}
```

#### Resposta de Sucesso (`200 OK`): Retorna `UserResponse` com o novo perfil atualizado.

---

### 8.4. Excluir Usuário
* **Método**: `DELETE`
* **Rota**: `/api/v1/users/{id}`
* **Headers**: `Authorization: Bearer <admin_token>`

#### Resposta de Sucesso (`204 No Content`): Sem corpo.

---

## Exemplos Rápidos com cURL

### 1. Criar Moto com Ficha Técnica:
```bash
curl -X POST http://localhost:8080/api/v1/motorcycles \
  -H "Authorization: Bearer <admin_ou_user_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "modelName": "Classic 350",
    "family": "Classic",
    "engineCc": 349,
    "startingPrice": 20990.00,
    "description": "O clássico reinventado",
    "active": true,
    "technicalSpec": {
      "powerHp": "20.2 hp",
      "torqueNm": "27 Nm",
      "weightKg": 195.00,
      "fuelCapacityL": 13.00,
      "seatHeightMm": 805
    }
  }'
```

### 2. Auto-Cadastro no Painel:
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Recrutador Tech",
    "email": "recrutador@empresa.com",
    "password": "senhaSegura123"
  }'
```

### 3. Iniciar Login (2FA):
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "recrutador@empresa.com",
    "password": "senhaSegura123"
  }'
```

### 4. Validar Código 2FA e Obter JWT:
```bash
curl -X POST http://localhost:8080/api/v1/auth/verify-2fa \
  -H "Content-Type: application/json" \
  -d '{
    "email": "recrutador@empresa.com",
    "code": "123456"
  }'
```

### 5. Promover Visitante para Operador (Admin):
```bash
curl -X PATCH http://localhost:8080/api/v1/users/{userId}/role \
  -H "Authorization: Bearer <admin_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "role": "ROLE_USER"
  }'
```


