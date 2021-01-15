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
    import codecs

    positive = []
    posneg = []

    pos = codecs.open("./content.txt", 'rb', encoding='UTF-8')

    while True:
        line = pos.readline()
        line = line.replace('\n', '')
        positive.append(line)
        posneg.append(line)
        
        if not line: break
    pos.close()
    import requests
    import re
    import pandas as pd
    test = (argument)
    content1 = []
    for i in range(len(posneg)):
        if test.find(posneg[i]):
            continue
        elif posneg[i] =='':
            continue
        else:
            content1.append(posneg[i])
                    
    print(content1)
    train_data = pd.read_csv("./title_datas1-Copy1.csv")
    test_data = pd.read_csv("./title_datas2-Copy1.csv")
    stopwords = ['의', '가', '이', '은', '들', '는', '좀', '잘', '걍', '과', '도', '를', '으로', '자', '에', '와', '한', '하다','음식','추천해줘','추천','해주다']

    import konlpy 
    from konlpy.tag import Okt
    okt = Okt()
    X_train = []
    for sentence in train_data['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거
        X_train.append(temp_X) 
        
    X_test = [] 
    for sentence in test_data['title']: 
        temp_X = [] 
        temp_X = okt.morphs(sentence, stem=True) # 토큰화 
        temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
        X_test.append(temp_X)
    
    Test1 = []
    temp_X = [] 
    temp_X = okt.morphs(test, stem=True) # 토큰화 
    temp_X = [word for word in temp_X if not word in stopwords] # 불용어 제거 
    Test1.append(temp_X)

    import numpy as np 

    from keras.preprocessing.text import Tokenizer
    max_words = 35000 
    tokenizer = Tokenizer(num_words = max_words) 
    tokenizer.fit_on_texts(X_train) 
    X_train = tokenizer.texts_to_sequences(X_train)
    X_test = tokenizer.texts_to_sequences(X_test)
    Test1 = tokenizer.texts_to_sequences(Test1)

    from keras.layers import Embedding, Dense, LSTM
    from keras.models import Sequential 
    from keras.preprocessing.sequence import pad_sequences 
    max_len = 3 # 전체 데이터의 길이를 20로 맞춘다 
    Test1 = pad_sequences(Test1, maxlen=max_len)
 
    from keras.models import load_model
    model1 = load_model('mnist_mlp_model.h5')#맵
    predict1 = model1.predict(Test1)
    import numpy as np
    predict_labels1 = np.argmax(predict1, axis=1) 
    result = []
    if(predict_labels1[0]==0):
        result.append('1') #달
    if(predict_labels1[0]==1):
        result.append('2') #맵
    if(predict_labels1[0]==2):
        result.append('3') #상
    if(predict_labels1[0]==3):
        result.append('4') #느끼
    if(predict_labels1[0]==4):
        result.append('5') #X
    print(result)



if __name__ == "__main__":
    main(sys.argv[1:])