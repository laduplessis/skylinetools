# Skyline Tools

Package of useful utilities and distributions to use with skyline models in BEAST2. For now the package only supports the birth-death skyline (BDSKY) and multitype birth-death process (BDMM), but there is no reason these utilities cannot be used for coalescent skyline models.

- [**Documentation**](doc/)
- [**Examples**](examples/)
- [**Unit tests**](test/)

[![Build Status](https://travis-ci.com/laduplessis/skylinetools.svg?branch=master)](https://travis-ci.com/laduplessis/skylinetools)

## Package contents

- *Parameters*
	- **RealParameterSequence:** Shortcut to create a linear sequence of real numbers with equal increments.
- *Smoothing priors*
	- **Brownian-motion smoothing prior:** Time-aware smoothing prior for a skyline parameter based on a Brownian-motion process. (Similar to the GMRF used for the Skyride/Skygrid). 
	- **Ornstein-Uhlenbeck smoothing prior:** Time-aware smoothing prior for a skyline parameter based on an Ornstein-Uhlenbeck proccess.
- *Utilities*
	- **TreeSlicer:** Interface for getting times on a tree.
	- **TreeSliceDateLogger:** Log dates of treeslice times.
	- **TreeDateLogger:** Log treeheight or other parameters as dates.
	
	
---
_Louis du Plessis, 2018_




