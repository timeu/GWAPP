FROM timeu/docker-gwas-base
MAINTAINER Uemit Seren <uemit.seren@gmail.com>
LABEL version="0.1"

RUN useradd --uid=10372 -ms /bin/bash gwas-web

RUN mkdir /srv/data
VOLUME ['/srv/data']

WORKDIR /gwapp

RUN /env/bin/pip install --upgrade pip
## required
RUN apt-get update && apt-get install -y ca-certificates

RUN /env/bin/pip install numpy==1.9.0 && \
    /env/bin/pip install numexpr==2.0.1 && \
    /env/bin/pip install Cython && \
    /env/bin/pip install && \
    /env/bin/pip install tables==2.4.0 && \
    /env/bin/pip install scipy==0.18.1 && \
    /env/bin/pip install h5py==2.5.0 && \
    /env/bin/pip install matplotlib==1.1.1 && \
    /env/bin/pip install CherryPy==3.2.2 && \
    /env/bin/pip install simplejson==2.0.9 && \
    /env/bin/pip install -e git+https://github.com/google/google-visualization-python.git#egg=gviz_api

COPY ./ /gwapp/

RUN git clone git://git.assembla.com/atgwas.git
# becacuse of https://github.com/docker/docker/issues/18501
RUN chown -R gwas-web:root /gwapp && chmod -R 755 /gwapp

# required because of https://github.com/docker/docker/issues/20240
RUN cd backend && ln -s static ../frontend/target/gwaswebapp-0.0.1-SNAPSHOT/ && cd public && ln -s resources ../../frontend/src/main/java/com/gmi/gwaswebapp/client/resources/ && cd /gwapp

RUN echo "cm_dir = /srv/data/dataset/" > /home/gwas-web/.gwa_config 


ENV PYTHONPATH=/gwapp/atgwas/src/:/gwapp/

USER gwas-web

EXPOSE 8080

ENTRYPOINT ["/env/bin/python" , "backend/__init__.py"]
