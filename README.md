# Exemplo de implementação do Masterpass em Java - SDK v7

Projeto exemplo que mostra a comunicação com o SDK do Masterpass com a versão 7. Para executar os exemplos é necessário:

- Criar a chave da APi diretamente no portal de desenvolvedores - https://developer.mastercard.com
- Copiar a chave p12 na raiz do projeto com o nome "sandbox.p12"
- Possuir maven instalado, com sua IDE de preferência (nesse caso, foi usado Eclipse Neon)
- Java >=8

## Hosts

O servidor, por padrão, escuta na porta 4567 e será necessário adicionar uma linha no hosts da sua máquina, pois a nova versão do Masterpass não suporta callback para localhost. O domínio temporário deve ser configurado no portal dos desenvolvedores. No caso desse exemplo, foi utilizado o domínio exemplo.mpass.com.br.

Hosts:
127.0.0.1 exemplo.mpass.com.br

Nota: as alterações realizadas dentro do portal dos desenvolvedores levam, em média, 30 minutos para propagarem.

## Sobre o ambiente Sandbox

É o ambiente de testes do Masterpass. Nesse ambiente é possível simular comportamentos idênticos ao do ambiente de produção. Cartões de testes estão disponíveis para uso, através do endereço: https://developer.mastercard.com/page/masterpass-testing

## Mais informações

Mais detalhes sobre a especificação podem ser encontrados em: [aqui](https://developer.mastercard.com/documentation/masterpass-merchant-integration/v7-Express#implementation-options1)


