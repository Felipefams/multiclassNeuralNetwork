# Azure Multiclass Neural Network
basic java multiclass neural network hosted in azure
## Problem
given some audio characteristics, classify Spotify songs in their proper class.
we will train the algorithm with various songs and their given class. Playlists
with pre set themes were used as a starting point, so for example, the songs of
a sad playlist, are used to train the algorithm about the what would a "sad" song be like.
## Data
all spotify tracks contain some audio characteristics. We will be using:
- acousticness
- danceability
- energy
- instrumentalness
- liveness 
- loudness 
- tempo
- valence

the meaning of each one of these words can be found in [spotify documentation](https://developer.spotify.com/documentation/web-api/reference/#object-audiofeaturesobject).
the [dataset](https://github.com/Felipefams/multiclassNeuralNetwork/blob/main/conda_env2.yaml) used can be found in the main page, and was generated by a [python script](https://github.com/Felipefams/multiclassNeuralNetwork/blob/main/requests-spotify.py)

## Steps
1. Create A Microsoft Azure Machine Learning Workspace
2. Import a dataset
3. Create a pipeline and train the model
4. Publish the model in a web service
5. download the files for the web service
6. test the model
7. [Java](https://github.com/Felipefams/multiclassNeuralNetwork/blob/main/src/Main.java) code to make the API calls

the .jar must be in the same folder of the Main.java
the java code must be run as:<b> <br>
`javac -cp json-simple-1.1.jar Main.java && java -cp .:json-simple-1.1.jar Main`


