# ☀️ App Previsão do Tempo

Aplicativo Android desenvolvido como atividade prática da disciplina **Programação de Dispositivos Móveis** da **UNIPAR EAD**, sob orientação do professor **Carlos Eduardo Simões Pelegrin**.

O projeto tem como objetivo aplicar os conhecimentos de desenvolvimento mobile utilizando **Java** no **Android Studio**, explorando conceitos como Activities, Fragments, consumo de API, Material Design e boas práticas de usabilidade.

---

## 📱 Descrição do Projeto

O **App Previsão do Tempo** permite ao usuário consultar informações meteorológicas em tempo real, exibindo temperatura, sensação térmica, umidade e previsão dos próximos dias.  
Além disso, o app conta com um **mapa interativo**, uma **tela de sobre com informações pessoais** e uma **Splash Screen** com animação de inicialização.

---

## 🎯 Funcionalidades Principais

- 🌤 **Consulta de clima atual** por cidade (consumo da API [HG Weather - HG Brasil](https://console.hgbrasil.com/documentation/weather))  
- 🗺️ **Exibição de mapa** com marcador fixo da cidade consultada  
- 📋 **Previsão estendida** dos próximos dias com uso de **RecyclerView** e **CardView**  
- 🚀 **Splash Screen** com exibição de logotipo por 3 segundos  
- 📑 **Tela de informações pessoais (Sobre)**  
- 🔍 **Busca dinâmica** para atualizar a cidade  
- 🎨 Interface moderna seguindo o **Material Design**  

---

## ⚙️ Tecnologias Utilizadas

- **Linguagem:** Java ☕  
- **IDE:** Android Studio  
- **API:** HG Brasil (Previsão do Tempo)  
- **Bibliotecas:**  
  - Retrofit – para consumo de API  
  - RecyclerView / CardView – para listagem de dados  
  - ZXing (opcional) – para leitura de QR Code  
  - Google Maps SDK – para exibição de mapa  
- **Design:** Material Design / Canva  

---

## 🧠 Conceitos Aplicados

- Uso de **Activities e Fragments**  
- Implementação de **RecyclerView** com **Adapter personalizado**  
- Integração com **API REST** usando Retrofit  
- Manipulação de **ViewModel** para compartilhamento de dados  
- Aplicação de **Material Design** (cores, ícones, AppBar, TabBar e botões)  
- Boas práticas de **UX/UI**  

---
## 🧩 Estrutura do Projeto

app/
  ├── manifests/
  │ └── AndroidManifest.xml
  ├── java/com/example/previsontempo/
  │ ├── adapters/
  │ ├── api/
  │ ├── fragments/
  │ ├── models/
  │ ├── AboutActivity.java
  │ ├── CityInputActivity.java
  │ ├── MainActivity.java
  │ ├── SplashActivity.java
  │ └── SharedViewModel.java
  ├── res/
  │ ├── drawable/
  │ ├── layout/
  │ ├── menu/
  │ ├── mipmap/
  │ └── values/

---

## 🚀 Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone https://github.com/RafaelFalaschii/AppPrevis-oDoTempo.git
2. Abra o projeto no Android Studio

3. Execute o emulador Android (API 33 ou superior)

4. Clique em Run ▶️ para compilar o app
