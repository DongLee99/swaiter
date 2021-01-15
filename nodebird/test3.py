import pandas as pd
import base64
import sys, getopt, time



def main(argv):
    argument = ''
    usage = 'usage: script.py -f <sometext>'
    
    # parse incoming arguments
    try:
        opts, args = getopt.getopt(argv,"hf:",["foo="])
    except getopt.GetoptError:
        print(usage)
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print(usage)
            sys.exit()
        elif opt in ("-f", "--foo"):
            argument = arg

    # print output

    train_data1 = pd.read_csv("./title_datas1.csv")
    test_data1 = pd.read_csv("./title_datas2.csv")
    train_data2 = pd.read_csv("./title_datas3.csv")
    test_data2 = pd.read_csv("./title_datas4.csv")
    
    test = (argument)
    stopwords = ['의', '가', '이', '은', '들', '는', '좀', '잘', '걍', '과', '도', '를', '으로', '자', '에', '와', '한', '하다','음식','추천해줘','추천','해주다']
    #from konlpy import init_jvm
    import konlpy
    from konlpy.tag import Okt
    #init_jvm("<JAVA_HOME>")
    okt = Okt()
    X_train = []
    for sentence in train_data1['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거
        X_train.append(temp_X) 
        
    X_test = [] 
    for sentence in test_data1['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
        X_test.append(temp_X)
    X_train2 = []
    for sentence in train_data2['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거
        X_train2.append(temp_X) 
        
    X_test2 = [] 
    for sentence in test_data2['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
        X_test2.append(temp_X)
    Test1 = []
    temp_X = [] 
    temp_X = okt.morphs(test, stem=True) # 토큰화 
    temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
    Test1.append(temp_X)
    Test2 = []
    temp_X = [] 
    temp_X = okt.morphs(test, stem=True) # 토큰화 
    temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
    Test2.append(temp_X)
    from keras.preprocessing.text import Tokenizer
    max_words = 35000 
    tokenizer = Tokenizer(num_words = max_words) 
    tokenizer.fit_on_texts(X_train) 
    X_train = tokenizer.texts_to_sequences(X_train)
    X_test = tokenizer.texts_to_sequences(X_test)
    Test1 = tokenizer.texts_to_sequences(Test1)
    from keras.preprocessing.text import Tokenizer
    max_words = 35000 
    tokenizer2 = Tokenizer(num_words = max_words) 
    tokenizer2.fit_on_texts(X_train2) 
    X_train2 = tokenizer2.texts_to_sequences(X_train2)
    X_test2 = tokenizer2.texts_to_sequences(X_test2)
    Test2 = tokenizer2.texts_to_sequences(Test2)

    from keras.layers import Embedding, Dense, LSTM
    from keras.models import Sequential 
    from keras.preprocessing.sequence import pad_sequences 
    max_len = 3 # 전체 데이터의 길이를 20로 맞춘다 
    Test1 = pad_sequences(Test1, maxlen=max_len)
    Test2 = pad_sequences(Test2, maxlen=max_len)
    from keras.models import load_model
    model2 = load_model('mnist_mlp_model.h4')#달
    model1 = load_model('mnist_mlp_model.h5')#맵
    predict2 = model2.predict(Test2)
    import numpy as np
    predict_labels1 = np.argmax(predict2, axis=1) 
    result = []
    if(predict_labels1[0]==1):
        result.append(1) 
    
    
    predict1 = model1.predict(Test1)
    
    import numpy as np
    predict_labels1 = np.argmax(predict1, axis=1) 
    if(predict_labels1[0]==1):
        result.append(2) 
    
    
    print(result)


if __name__ == "__main__":
    main(sys.argv[1:])