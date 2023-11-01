import string
import ssl
import pandas as pd
import numpy as np
import tensorflow as tf

import nltk
from nltk import word_tokenize          
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords

from sklearn import svm
from sklearn.naive_bayes import MultinomialNB
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfTransformer, CountVectorizer, TfidfVectorizer
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix

#=============================================
#                 Auxiliary
#=============================================

# Mapping dictionaries
label_mapping = {'TRUTHFULPOSITIVE': 0, 'TRUTHFULNEGATIVE': 1, 'DECEPTIVEPOSITIVE': 2,'DECEPTIVENEGATIVE': 3}
int_to_label =  { value: key for key, value in label_mapping.items() }


def StemTokenize(text):
        trans_table = str.maketrans("", "", string.punctuation)
        stemmer = nltk.RegexpStemmer('ing$|able$|$s', min = 4)

        text.rstrip()
        tokens = [word for word in nltk.word_tokenize(text.translate(trans_table)) if len(word) > 1]
        stems = [stemmer.stem(item) for item in tokens]
        return stems
    

class LemmaTokenizer(object):
    def __init__(self):
        self.wnl = WordNetLemmatizer()
    def __call__(self, articles):
        tokens = [self.wnl.lemmatize(t) for t in word_tokenize(articles)]
        return [token.lower() for token in tokens]
    

def nltk_handler():
    try:
        _create_unverified_https_context = ssl._create_unverified_context
    except AttributeError:
        pass
    else:
        ssl._create_default_https_context = _create_unverified_https_context

    nltk.download('punkt')
    nltk.download('wordnet')
    nltk.download('stopwords')


def predict_results_labels(classifier, vectorizer, encoded=True):
    """
    Utilizes chosen vectorizer to create an input vector
    to predict the "test_just_reviews" labels with the desired 
    classifier into a "results.txt" file. Must flag if encoded or not.
     """
    
    # Create input vector from just the reviews
    results_input_vector = vectorizer.transform(just_reviews)
    results_input_vector = results_input_vector.toarray()

    results_out_file = open("results.txt", 'w')

    if encoded:
        # Predict encoded labels and convert to list
        results_predict = classifier.predict(results_input_vector)
        results_predict = np.argmax(results_predict, axis=1)
        results_predict = results_predict.astype(int).tolist()

        # Write into results.txt the decoded labels
        output = ""
        for prediction in results_predict:
            output += int_to_label[prediction] + "\n"
        results_out_file.write(output)

    else:
        results_predict = classifier.predict(results_input_vector)

        output = ""
        for result in list(results_predict):
            output += result + "\n"
        results_out_file.write(output)

    results_out_file.close()
        


#=============================================
#                   Models
#=============================================

def svm_classifier(labels, features):

    # Labels and Features are arrays
    dataset_review_list = features[1].tolist()

    # PRE-PROCESSING FEATURES: Unigram and Bigrams, Stop Words, Lemmatization, Strip Accents and Lowercasing
    x_train_tfidf = stop_tfidf_vectorizer.transform(dataset_review_list)

    # Convert to NP arrays
    y = np.array(labels[0])
    X = x_train_tfidf.toarray()

    #Splitting dataset into training and testing dataset
    X_train, X_test, Y_train, Y_test = train_test_split(X, y, test_size=0.05, random_state=47951)

    # Create SVM object
    support = svm.SVC(C=3, kernel='sigmoid')

    support.fit(X_train, Y_train)
    predicted= support.predict(X_test)
    score=accuracy_score(Y_test,predicted)

    print("Your Model Accuracy is", score)

    predict_results_labels(support, stop_tfidf_vectorizer, False)


def neural_network(labels, features, n=1):

    dataset_review_list = features[1].tolist()

    # PRE-PROCESS FEATURES: Unigrams and Bigrams, Lemmatization, Strip Accents and Lowercasing
    x_train_tfidf = tfidf_vectorizer.transform(dataset_review_list)

    y = labels[0]
    X = x_train_tfidf.toarray()

    # Apply mapping to data labels
    for i in range(len(y)):
        y[i] = label_mapping[y[i]]

    # Convert to numpy arrays
    y = np.array(y)

    #Splitting dataset into training and testing dataset
    X_train, X_test, Y_train, Y_test = train_test_split(X, y, test_size=0.05, random_state=0)

    #Encode sets for 4 numeric label values
    Y_train = tf.one_hot(Y_train, 4)
    Y_test = tf.one_hot(Y_test, 4)

    train_prediction = None
    test_prediction = None

    for i in range(n):
        ann = tf.keras.models.Sequential()

        #Adding Hidden Layers
        ann.add(tf.keras.layers.Dense(units=16,activation="tanh"))
        ann.add(tf.keras.layers.Dense(units=16,activation="sigmoid"))
        ann.add(tf.keras.layers.Dense(units=16,activation="tanh"))
        
        #Adding Output Layer
        ann.add(tf.keras.layers.Dense(units=4,activation="softmax")) #units is 4 because we have 4 output categories, see label mapping

        #custom_opt = tf.keras.optimizers.Adam(learning_rate=0.005)

        #Compiling added layers with optimizer and loss functions
        ann.compile(optimizer='adam',loss="cosine_similarity",metrics=['accuracy'])

        #Train ANN
        ann.fit(X_train, Y_train, batch_size=8, epochs=12)

        if train_prediction is None or test_prediction is None:
            train_prediction = ann.predict(X_train)
            test_prediction = ann.predict(X_test)
        else:
            train_prediction += ann.predict(X_train)
            test_prediction += ann.predict(X_test)

    # Predicted labels = max value of each row
    final_labels_train = np.argmax(train_prediction, axis=1)
    final_labels_test = np.argmax(test_prediction, axis=1)

    # Print the confusion matrix and classification report for training data
    print("Confusion Matrix (Training Data):")
    print(confusion_matrix(np.argmax(Y_train, axis=1), final_labels_train))
    print("\nClassification Report (Training Data):")
    print(classification_report(np.argmax(Y_train, axis=1), final_labels_train))

    # Print the confusion matrix and classification report for test data
    print("\nConfusion Matrix (Test Data):")
    print(confusion_matrix(np.argmax(Y_test, axis=1), final_labels_test))
    print("\nClassification Report (Test Data):")
    print(classification_report(np.argmax(Y_test, axis=1), final_labels_test))

    score=accuracy_score(np.argmax(Y_test, axis=1), final_labels_test)
    print("Your Model Accuracy is", score)

    predict_results_labels(ann, tfidf_vectorizer)


def bayes(labels, features, n=1):
    """
    A Naive Bayes model. Requires features to be pre-processed as TF-IDF and labels to be a list/np-array.
    """

    # Convert all features into a list
    dataset_review_list = features[1].tolist()

    # PRE-PROCESSING FEATURES: Unigram and Bigrams, Stop Words, Lemmatization, Strip Accents and Lowercasing
    x_train_tfidf = stop_tfidf_vectorizer.transform(dataset_review_list)

    # Set training processed labels and features
    X = x_train_tfidf
    y = np.array(labels[0])
    n_right = 0

    for i in range(n):
        train_x, test_x, train_y, test_y = train_test_split(X, y, test_size=0.1)

        clf = MultinomialNB().fit(train_x, train_y)
        y_score = clf.predict(test_x)

        for i in range(len(y_score)):
            if y_score[i] == test_y[i]:
                n_right += 1

    print("Accuracy: %.2f%%" % ((n_right/float(len(test_y)*n) * 100)))

    predict_results_labels(clf, stop_tfidf_vectorizer, False)

#=============================================
#                   Main
#=============================================

#STOP WORDS
final_stopwords = list([w for w in stopwords.words("english") if w not in ['no','not','as','but']])
final_stopwords += ['ha', 'le', 'u', 'wa', "d", "'ll", "'re", "'s", "'ve", 'could', 'doe', 'might', "'d", 'must', "n't", 'need', 'sha', 'wo', 'would']

# FORMAT TRAINING DATASET
df = pd.read_csv('train.txt', sep="\t", header=None)
labels = pd.DataFrame(df[0])
features = pd.DataFrame(df[1])

# DEFINE VECTORIZERS
tfidf_vectorizer = TfidfVectorizer(
        ngram_range=(1,2), 
        tokenizer=LemmaTokenizer(), 
        strip_accents='unicode', 
        lowercase=True, 
        min_df=2, 
        use_idf=False
    )

stop_tfidf_vectorizer = TfidfVectorizer(
        ngram_range=(1,2), 
        stop_words=final_stopwords, 
        tokenizer=LemmaTokenizer(), 
        strip_accents='unicode', 
        lowercase=True, 
        min_df=2, 
        use_idf=False
    )

# FIT VECTORIZERS TO CREATE "VOCABULARY"
just_reviews_file = open("test_just_reviews.txt", 'r')
just_reviews = list(just_reviews_file.readlines())
dataset_reviews = features[1].tolist()

tfidf_vectorizer.fit(just_reviews)
tfidf_vectorizer.fit(dataset_reviews)
stop_tfidf_vectorizer.fit(just_reviews)
stop_tfidf_vectorizer.fit(dataset_reviews)


# CHOSEN MODEL TO RUN
#bayes(labels, features, 50)
neural_network(labels, features, 50)
#svm_classifier(labels, features)

# CLEANUP
just_reviews_file.close()
