#!/bin/bash

__directory="$(readlink -f "$(dirname "${BASH_SOURCE[0]}")")"

function help() {
  echo "keycloak-client.sh [options]"
  echo "  --debug-port <port>       Set debug port, default 9000"
  echo "  --debug-suspend           Suspend debug to wait for debugger to be attached"
  echo "  --socks5 <host> <port>    SOCKS5 proxy, default localhost / 8001"
  echo "  --profile <profile>       Run the application in the specified profile configuration"
  echo "  --ssl                     Enable SSL, default disabled"
}

__debug_suspend="n"
__debug_port=9000
__socks5_options=""
__profile=""
__ssl="-Dserver.ssl.enabled=false"

export KEYCLOAK_CLIENT_OPTS=""
export APPHOME=${__directory}

while [ "$1" != "" ]; do
  case "$1" in
  --debug-suspend)
    __debug_suspend="y"
    ;;

  --debug-port)
    shift
    __debug_port=${1}
    ;;

  --socks5)
    shift
    __socks5_host=${1}
    shift
    __socks5_port=${1}
    __socks5_options="-DsocksProxyHost=${__socks5_host} -DsocksProxyPort=${__socks5_port}"
    ;;

  --profile)
    shift
    __profile="-Dspring.profiles.active=${1}"
    ;;

  --ssl)
    __ssl="-Dserver.ssl.enabled=true"
    __ssl="${__ssl} -Djavax.net.ssl.keyStore=${APPHOME}/keystores/keycloak-client.p12 -Djavax.net.ssl.keyStorePassword=oassword -Djavax.net.ssl.keyStoreType=pcks12"
    __ssl="${__ssl} -Djavax.net.ssl.trustStore=${APPHOME}/keystores/truststore.jks -Djavax.net.ssl.trustStorePassword=password -Djavax.net.ssl.trustStoreType=jks"
    ;;

  h|-h|--h|help|-help|--help)
    help
    exit 0
    ;;

  *)
    echo "Error: Invalid argument $1"
    help
    exit 1
    ;;
  esac
  shift
done

export JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,address=${__debug_port},suspend=${__debug_suspend} ${__socks5_options} ${__profile} ${__ssl}"

${__directory}/bin/keycloak-client