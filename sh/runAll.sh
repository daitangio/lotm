#!/bin/sh

cd $(dirname $0)/..
# cd ${LOTM_HOME}
set -xeo pipefail
pwd
caddy run --config etc/Caddyfile --watch