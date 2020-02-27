#!/usr/bin/env bash
set -e
set -o pipefail

appname=$(echo "$1" | tr -dc '[:alnum:]\n\r' | tr '[:upper:]' '[:lower:]')
[[ -z "$appname" ]] && { echo "Appname passed is empty" ; exit 1; }
echo "Setting up local env"
[[ -d "skeleton" ]] && { echo "Skeleton already exists" ; exit 1; }

mkdir -p .idea/runConfigurations
cp skeleton.xml .idea/runConfigurations/skeleton.xml

cat ./.idea/runConfigurations/skeleton.xml | awk -v appname="$appname" '{gsub(/skeleton/,appname)}1' > temp
mv temp .idea/runConfigurations/skeleton.xml

echo "Please restart IntelliJ"