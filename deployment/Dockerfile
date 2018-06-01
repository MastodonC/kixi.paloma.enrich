FROM zittix/docker-baseimage-java8
MAINTAINER Jason Bell <jason@mastodonc.com>

CMD ["/sbin/my_init"]

RUN apt-get update \
&&  apt-get -y install software-properties-common

ENV JAR_LOCATION=/srv/kixi.paloma.enrich.jar
ENV CONFIG_PROFILE=${environment}

ADD target/uberjar/kixi.paloma.enrich-standalone.jar /srv/kixi.paloma.enrich.jar
ADD scripts/run.sh /etc/service/kixi.paloma.enrich/run

RUN apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*