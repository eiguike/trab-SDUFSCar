###Sistemas Distribuídos 2015/2, Trabalho 1 - Multicast ordenado

Para executar em terminal a aplicação, que consiste de três diferentes processos e uma rotina de comando, os arquivos devem ser compilados e executados separadamente.
Para compilar todos os arquivos necessários, basta executar os seguinte comandos dentro da pasta 'src' do projeto:
javac Servidor/P1.java Servidor/Server.java Servidor/Client.java Servidor/Node.java
javac Servidor/P2.java Servidor/Server.java Servidor/Client.java Servidor/Node.java
javac Servidor/P3.java Servidor/Server.java Servidor/Client.java Servidor/Node.java
javac Servidor/Principal.java Servidor/Server.java Servidor/Client.java Servidor/Node.java

Com os arquivos compilados, basta executar cada um dos seguintes comandos em instancias diferentes do terminal:
java Servidor.P1
java Servidor.P2
java Servidor.P3
java Servidor.Principal
