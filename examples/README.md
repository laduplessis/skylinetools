# Examples

## 1. OUPrior

### 1.1. OUPrior Tests

`ouprior_test1.xml`-`ouprior_test5.xml`: Tests to check that the OU-prior implemented in BEAST2 is correct. Check that MCMC samples correct trajectories and that it is possible to estimate parameters given a trajectory. ([Results](smoothingpriors/OUPriorTests.md))


### 1.2. OUPrior examples

**With OUPrior**

1. `hcv_bdsky_ouprior.xml`: Egyptian HCV dataset (contemporaneous sampling only), with relative times (between 0 and 1). OUPrior on reproductive number with dimension 40.
2. Serial sampling and real times (Ebola)

**Comparison xml files (without OUPrior)**

1. `hcv_bdsky.xml`: Egyptian HCV dataset (contemporaneous sampling only), reproductive number with dimension 40.



---


## 2. TreeSlicer 

### 2.1. TreeSlicer Tests
Tests to compare the result of using TreeSlicer to obtaining the same or similar results in a more complicated way without it. Note that some XMLs log more statistics than necessary and contain some unnecessary RPN calculators to check correctness of the results. 

**With TreeSlicer**

1. `dengue4_bdsky_equidistant5_treeslicer.xml`: 5 intervals for R_e, equally spaced between tmrca and present, sampling proportion 0 before oldest sample.
2. `dengue4_bdsky_fixeddates_treeslicer.xml`: 5 intervals for R_e at specific fixed dates, sampling proportion 0 before oldest sample.
3. `dengue4_bdsky_csc_treeslicer.xml`: 2 intervals for R_e, with the shift-date estimated.
4. `dengue4_bdsky_csc_treeslicer_hierarchical.xml`: 2 trees, each with 2 intervals for R_e, with a shared shift-date, which is estimated.
5. `dengue4_bdsky_equidistant5_treeslicer_hierarchical.xml`: 2 trees, each with the same 3 shared intervals for R_e.

**Comparison xml files (without TreeSlicer)**

1. `dengue4_bdsky_equidistant5.xml`: 5 intervals for R_e, equally spaced between origin and present, sampling proportion 0 before oldest sample.
2. `dengue4_bdsky_equidistant5_rootcondition.xml`: 5 intervals for R_e, equally spaced between tmrca and present, sampling proportion 0 before oldest sample (no origin estimated).
3. `dengue4_bdsky_fixeddates.xml`: 5 intervals for R_e at specific fixed dates, sampling proportion 0 before oldest sample.
4. `dengue4_bdsky_csc.xml`: 2 intervals for R_e, with the shift-time estimated.



### 2.2  TreeSlicer Examples
_Coming soon_


---
_Louis du Plessis, 2018_