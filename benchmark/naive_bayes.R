library(e1071)
library(datasets)
classifier<-naiveBayes(iris[,1:4], iris[,5]) 
con_mat<- table(predict(classifier, iris[,-5]), iris[,5], dnn=list('predicted','actual'))

