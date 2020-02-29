#!/usr/bin/env bash

#killing existing tunneling to postgres
ps aux | grep -ie 'ssh -f -N -L 4567:bounce-staging-rds.cavqxjfmp25f.ap-south-1.rds.amazonaws.com:5432' | grep -Fv 'grep -ie ssh -f -N -L' | awk '{print $2}' | xargs kill -9

#killing existing tunneling to redis
ps aux | grep -ie 'ssh -f -N -L 1234:redis-staging-bcore.n6pp0w.0001.aps1.cache.amazonaws.com:6379' | grep -Fv 'grep -ie ssh -f -N -L' | awk '{print $2}' | xargs kill -9

#staging postgres config. Local port 4567 configured to postgres
ssh -f -N -L 4567:bounce-staging-rds.cavqxjfmp25f.ap-south-1.rds.amazonaws.com:5432 $1@10.7.0.1

#staging redis config. Local port 1234 configured to redis.
ssh -f -N -L 1234:redis-staging-bcore.n6pp0w.0001.aps1.cache.amazonaws.com:6379 $1@10.7.0.1