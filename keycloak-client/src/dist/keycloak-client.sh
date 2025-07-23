#!/bin/bash

__directory="$(readlink -f "$(dirname "${BASH_SOURCE[0]}")")"

function help() {
  echo "keycloak-client.sh [options]"
  echo "  --debug-port <port>  Set debug port, default 9000"
  echo "  --debug-suspend      Suspend debug to wait for debugger to be attached"
}

__debug_suspend="n"
__debug_port=9000

while [ "$1" != "" ]; do
  case "$1" in
  --debug-suspend)
    __debug_suspend="y"
    ;;

  --debug-port)
    shift
    __debug_port=${1}
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

export JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,address=${__debug_port},suspend=${__debug_suspend}"
export KEYCLOAK_CLIENT_OPTS=""

${__directory}/bin/keycloak-client