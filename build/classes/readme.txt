Names: Haoyu Xiong, Jingya Wu, Iris Fu

Primary resources:
    1. Java Standard Edition 8 API
    2. stackoverflow.com
    3. tutorialspoint.com
    4. Big Java

Format of configuration file:
    The configuration file is a txt file. The first line contains the number of inputs, number of outputs, number of layers (including the input and output layers), number of neurons per hidden layer (assume each hidden layer has the same number of neurons), and the maximum SSE specified by the user in order, and separated by comma. Starting from the second line, there are numOut number of "blocks" (numOut is the number of outputs). Each line of a block contains weights of one perceptron, starting from the hidden layer right behind the input layer, to the output layer, also separated by comma. The numOut number of "blocks" will then iterate through all outputs. An example configuration file is provided in example.txt. This example is for an ANN with 2 inputs, 2 outputs, 2 hidden layers (4 layers in total), 3 nodes per hidden layer, and a max SSE of 0.001. The nodes are numbered from 0 to 9 like shown below, and the weight between one node and another is intentionally set (0.12 for weight between node 1 and 2 or 0.26 for weight between node 2 and 6), so that it is clearer how we stored the weights in the configuration file.

              ---        ---
             | 2 |      | 5 |
              ---        ---
     ---                          ---
    | 0 |                        | 8 |
     ---      ---        ---      ---
             | 3 |      | 6 |
     ---      ---        ---      ---
    | 1 |                        | 9 |
     ---                          ---
              ---        ---
             | 4 |      | 7 |
              ---        ---
    input         hidden         output

Format of Trainging Log:
    Training Log will print out the weights for each inputs, with each line for a single node. Epoch number will not increase until all lines have been run. The program will print out the weights for the first output first, and then for the second output and so on.