rm(list = ls())
library(bdskytools)

par(mfrow=c(2,1))

#####################
# ouprior_test1.xml #
#####################

# Calculate density of OU-process and get percentiles (blue and red)
t     <- seq(0,9,by=1)
x0    <- 10
mu    <- 3
sigma <- 1
nu    <- 0.1
X     <- plotOUProcessHPD(x0, t, mu, sigma, nu, ylim = c(0,12), main='ouprior_test1.xml')
sim_hpd <- getMatrixHPD(X)

# Sample from posterior in BEAST2 and get HPD (orange and green)
lf <- readLogfile("ouprior_test1.log")

xsample <- getSkylineSubset(lf,"x")
for (i in 1:nrow(xsample)) {
  lines(t,xsample[i,],col=pal.dark(corange,0.1),lwd=0.5)
}
sample_hpd <- getMatrixHPD(xsample)
lines(t,sample_hpd[1,],col=pal.dark(cgreen),lty=2,lwd=2)
lines(t,sample_hpd[2,],col=pal.dark(cgreen),lty=2,lwd=2)
lines(t,sample_hpd[3,],col=pal.dark(cgreen),lty=2,lwd=2)


#####################
# ouprior_test2.xml #
#####################

# Simulate OU-trajectory and get HPD (blue and red)
t     <- seq(0,9,by=1)
x0_p  <- getPrior(runif,1,min=20,max=22)
mu    <- 20
sigma <- 1
nu    <- 0.1
X     <- plotOUProcessHPDEmpirical(x0_p, t, mu, sigma, nu, ylim = c(15,25), main='ouprior_test2.xml')
sim_hpd <- getMatrixHPD(X)

# Sample from posterior in BEAST2 and get HPD (orange and green)
lf <- readLogfile("ouprior_test2.log")

xsample <- getSkylineSubset(lf,"x")
for (i in 1:nrow(xsample)) {
    lines(t,xsample[i,],col=pal.dark(corange,0.1),lwd=0.5)
}
sample_hpd <- getMatrixHPD(xsample)
lines(t,sample_hpd[1,],col=pal.dark(cgreen),lty=2,lwd=2)
lines(t,sample_hpd[2,],col=pal.dark(cgreen),lty=2,lwd=2)
lines(t,sample_hpd[3,],col=pal.dark(cgreen),lty=2,lwd=2)