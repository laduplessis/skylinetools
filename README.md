# Skyline Tools

Package of useful utilities and distributions to use with skyline models in BEAST2. For now the package only supports the birth-death skyline (BDSKY) and multitype birth-death process (BDMM), but there is no reason these utilities cannot be used by other models.

- [**Documentation**](https://github.com/laduplessis/skylinetools/wiki)
- [**Examples**](examples/)
- [**Unit tests**](test/)

[![Build Status](https://travis-ci.com/laduplessis/skylinetools.svg?branch=master)](https://travis-ci.com/laduplessis/skylinetools)

## Package contents

- *Parameters*
	- **RealParameterSequence:** Shortcut to create a linear sequence of real numbers with equal increments.
- *Smoothing priors*
	- **Brownian-motion smoothing prior:** Time-aware smoothing prior for a skyline parameter based on a Brownian-motion process. (Similar to the GMRF used for the Skyride/Skygrid). _(not fully tested)_
	- **Ornstein-Uhlenbeck smoothing prior:** Time-aware smoothing prior for a skyline parameter based on an Ornstein-Uhlenbeck process. _(not fullly tested)_
- *Utilities*
	- **TreeSlicer:** Interface for getting times on a tree.
	- **TreeSliceDateLogger:** Log dates of treeslice times.
	- **TreeDateLogger:** Log treeheight or other parameters as dates.


## Installation

To install Skyline Tools:

1. Download and install [BEAST2](www.beast2.org).
2. Launch the BEAUti application distributed with BEAST.
3. From the File menu select "Manage Packages".
4. Click the "Package repositories" button at the bottom of the Package Manager dialog box.
5. Select "Add URL" and enter the following URL: `https://laduplessis.github.io/skylinetools/package.xml`.
7. Click the "Done" button, then select "skylinetools" from the list of packages.
8. Click the "Install/Upgrade" button. Once installation is complete, XML files using SkylineTools can be run from BEAST2.


## Citation

If you use TreeSlicer in any of your XML files please add a link to this repository.

## License

This package is distributed under the GNU General Public Licence version 3, which is contained in this directory in the file named COPYING.


---
_Louis du Plessis, 2018_
