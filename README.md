GWAPP -  A Web Application for Genome-wide Association Mapping in A. thaliana
=====

## Overview

GWAPP is a Standalone GWAS Web Application that enables reseachers working with Arabidopsis thaliana to do Genome Wide Association Mapping (GWAS) on their phenotypes. 

It can be accessed by following site: http://gwapp.gmi.oeaw.ac.at. 

## VM Image 

We also provide a production ready virtual box image of GWAPP which can be downloaded and run locally on any workstation or vmware cluster. All required packages are pre-configured. The machine was built on Ubuntu LTS 12.04 with an appropriate set of packages. We package the VM as an OVA template that should be importable into all of vmware's products and for Oracle's VirtualBox solution. 



## DOWNLOAD 

http://gridftp.gmi.oeaw.ac.at/gwas-vm-shipping-2.ova

## Configuration & Settings:

The VM has some default configuration settings and paths:

* Web-application: /srv/gwas-app/gwas-web-app
* GWAS library: /srv/atgwas 
* Data-folder: /srv/data
* Genotype data: /srv/data/genotype
* HDF5 result files (per user): /srv/data/datasets
* Username: root
* Password: rootroot
 
The `CherryPy` application server runs on port `8080` and the `nginx` web-server runs on the default port `8081` and forwards the calls to the `CherryPy` application server. 
Supervisord will make sure that the `CherryPy` application server is started when the vmware image boots up. 
