# PJBL - Hash

Grupo: Eduardo Theodoro | Felipe Maestri | Gustavo Muniz | Vinicius Marcon

## Objetivo

Implementar e analisar o desempenho de diferentes funções Hash usando Java. Rodar 3 funções Hash, com 3 diferentes tamanhos de vetores com 3 conjuntos de dados gerados aleatoriamente. (O relatório com as análises e gráficos está em RELATORIO.md)

## Escolhas e Justificativas

Para nosso trabalho, escolhemos 3 funções Hash específicas, duas usando listas encadeadas (Multiplication Hash & Mix Hash), para ter buckets de dados em índices com colisões, e a outra com rehashing, utiliza uma técnica de rehashing duplo (aplica de forma inicial a Mix Hash e em seguida a Double Hash Step Mix).

#### Multiplication Hash

- Escolhemos essa pois ela performava relativamente mais rápida e com menos colisões que a Modular Hash, ela espalha bem as chaves e é simples. A multiplicação espalha os bits da chave e o operador de parte fracionária “embaralha” o resultado dentro do intervalo [0, 1).
  Multiplicamos depois por m para transformar esse valor fracionário em um índice de tabela (0..m-1).

#### Mix Hash

- Escolhida também por ser rápida e simples de implementar. Funciona com uma lógica de operações bitwise e multiplicações por números grandes para embaralhar os bits de uma chave inteira. Utiliza XOR para quebrar padrões repetitivos.

#### Double Hash Step Mix

- Variação da Mix Hash para ser usada como passo do rehashing duplo. O retorno é ajustado para o intervalo [1..m-1] (ex.: 1 + (h2(key) % (m-1))), garantindo passo não nulo; com m primo, o passo é coprimo de m, assegurando ciclo completo na sondagem e evitando travamentos.

## Estrutura do projeto

```
src/
├── hash/
│   ├── DoubleHashStepMix.java
│   ├── MixHash.java
│   └── MultiplicationHash.java
│
├── io/
│   ├── DataReader.java
│   └── DataStreamer.java
│
├── model/
│   └── Registro.java
│
├── structure/
│   ├── HashFunction.java
│   ├── HashTable.java
│   ├── HashTableRehash.java
│   ├── LinkedList.java
│   └── Node.java
│
├── Generator.java
├── Main.java
├── MainBenchmark.java
└── MainStreamAll.java
```

#### src

- Generator:
  - Gera dados com a seed fixa (para ter replicabilidade -> Seed: 10)

- MainBenchmark:
  - Instancia as tabelas, e executa todos os métodos para o código funcionar, insere os dados nas tabelas Hash, faz as buscas, o relatório e salva os dados em um txt final.

- MainStreamAll:
  - Cria instâncias de DataStreamer para a criação dos dados diretamente em arquivos txt (por segurança e replicabilidade).

#### /io

- DataStreamer:
  - Função geradora, porém escreve tudo direto para memória (arquivo.txt) por segurança, ao invés de criar uma nova estrutura de dados.

- DataReader:
  - Lê os dados dos arquivos TXT e adiciona e cria objetos da classe Registro diretamente dentro das tabelas Hash.

#### /Model

- Registro:
  - Classe para representar os registros (elementos que vamos inserir).

#### /Structure

- Node:
  - Base da estrutura, recebe um valor (Classe Registro). Possui apenas os métodos básicos de retornar o código `getCodigo()` e `getCodigoString()`

- LinkedList:
  - Tem função de guardar as chaves que caem no mesmo índice da tabela (apelido: Bucket), utiliza a classe Node como base.
  - Seus métodos são:
    - `insertAndCount(int value)`: insere em ordem crescente e retorna quantos nós foram visitados (São as “colisões” do bucket).
    - `contains(int value)`: busca o valor aproveitando a ordenação.
    - `getSize()`: tamanho do bucket.

- HashFunction:
  - Interface que nos permite testar as diferentes funções Hash, serve de esqueleto para as funções.

- HashTable:
  - A tabela hash, um vetor onde cada elemento é uma linkedlist (bucket para encadeamento em caso de colisões).
  - Principais métodos:
    - `insert(int key)`: calcula índice com a função hash injetada, cria bucket se necessário e insere com `insertAndCount`, somando as colisões totais.
    - `contains(int key)`: calcula índice e delega ao bucket `contains`.
    - Getters: `getCollisions()` e `getSize()`.
    - `bucketLengths()`: Retorna um vetor com o tamanho de cada bucket.

- HashTableRehash:
  - Tabela hash com endereçamento aberto (double hashing) e resize automático para os casos de dados que superam o tamanho da tabela. Armazenando diretamente os registros em um vetor de slots.
  - Principais métodos:
    - Getters básicos
    - `insert()`: Insere os dados e caso não possua espaço chama `resize()`.
    - `resize()`: Dobra o tamanho do array e re-insere (`reinsertInto()`) todos os elementos.
    - `contains()`: Busca dentro da tabela.
    - `reinsertInto()`: Utilizado para reinserir os dados dentro do novo array em `resize()`.

#### /hash

- MixHash:
  - Implementa uma função de dispersão baseada em bit mixing, inspirada nos finalizadores do MurmurHash/xxHash.
	- Realiza uma sequência de operações de XOR, shift e multiplicações por constantes grandes para espalhar os bits da chave inteira e retorna um valor no intervalo [0, m - 1].

- MultiplicationHash:
  - Implementa o método da multiplicação proposto por Donald Knuth, utilizando uma constante irracional (A = (√5 - 1)/2) para multiplicar pela chave, extrair a parte fracionária do resultado e multiplicar por m, gerando o índice.
  
- DoubleHashStepMix:
  - Variação da MixHash usada exclusivamente como função de passo (step) no rehashing duplo, garantindo que o valor retornado esteja sempre no intervalo [1, m - 1], evitando passo zero. Quando m é primo, assegura que o passo seja coprimo de m, garantindo que toda a tabela possa ser percorrida. 
