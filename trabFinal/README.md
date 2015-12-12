Este projeto tem como objetivo o desenvolvimento de um protótipo denomidado MyTube que permite o upload e download de vídeos privados para um sistema web. Em linhas gerais o sistema deverá ser capas de (ver a arquitetura simplificada na Fig. 1):
- receber um vídeo e uma descrição textual do mesom através de um serviço web;
- armazenar o vídeo nos servidores backend; e
- permitir o download dos vídeos.

Utilizamos a linguagem Java para desenvolver o Web Service, o o servlet para receber as requisições do browser, uma máquina Amazon EC2 que contém o PostgreSQL que guardará o ID do vídeo no S3, e o Amazon S3 propriamente dito para guardar os vídeos na Cloud.

Para utilizar este projeto, você deve baixar o AWS SDK Java e importar todas as bibliotecas necessárias.
