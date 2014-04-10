f<- function(con_mat)
{
dimen<- dim(con_mat)

right=0
wrong=0
i=0
j=0
for (i in 1:dimen[1])
{ 
  for (j in 1:dimen[2])
  {if (i==j)
  {
    right=right+as.numeric(con_mat[i,j])
  }
   else
   {
     wrong=wrong+con_mat[i,j]
   }
  }
}
right
wrong
right/(right+wrong)

m=0
n=0
result<- numeric(0)
for (m in 1:dimen[1])
{ 
  temp1=0
  temp2=0
  for (n in 1:dimen[2])
  {if (m==n)
  {
    temp1=as.numeric(con_mat[m,n])
  }
   else 
   {
     temp2=temp2+con_mat[m,n]
   }
  }
  x=temp1/(temp1+temp2)
  result<- append(result,x)
}
#temp1
#temp2
#temp1/(temp1+temp2)
result
}