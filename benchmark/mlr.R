testidx <- which(1:length(iris[,1])%%5 == 0)
iristrain <- iris[-testidx,]
iristest <- iris[testidx,]
library(glmnet)
cv.fit <- cv.glmnet(as.matrix(iristrain[,-5]), iristrain[,5], alpha=0.8, family="multinomial")
prediction <- predict(cv.fit, newx=as.matrix(iristest[,-5]),type="class")
con_mat=table(prediction, iristest$Species)

