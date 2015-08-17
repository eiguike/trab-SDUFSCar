##Primeiro trabalho - Sistema Distribuídos 2015/2

##### Multicast Ordenado
>#####Instruções:
Considere um grupo de processos que enviam mensagens de multicast uns aos outros. Cada mensagem transporta sua marca de tempo. Uma mensagem de multicast também é enviada conceitualmente para o remetente. Quando um processo recebe uma mensagem ela é colocada numa fila, ordenada conforme a marca de tempo. O receptor envia mensagens de multicast de confirmação. A marca de tempo da mensagem de agradecimento é maior que a marca de tempo de recebimento. Um processo só pode entregar a mensagem para a aplicação quando essa mensagem estiver no início da fila e tiver sido reconhecida 
por cada um dos processos. Neste momento a mensagem é retirada da fila. Cada processe terá a mesma cópia da fila e as mensagens serão entregues na mesma ordem. Desenvolva um algoritmo e prove através de um exemplo que o multicast será totalmente ordenado.
