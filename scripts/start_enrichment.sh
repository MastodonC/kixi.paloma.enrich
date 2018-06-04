#!/bin/bash

TAG=latest
sudo docker run --name kixi.paloma.enrich mastodonc/kixi.paloma.enrich:${TAG} /srv/run
