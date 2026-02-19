# Namespace observabilidad

## creación del namespace

kubectl create namespace observability

## ¿pull? de helm

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

## Instalación con configuración básica

* helm install prom prometheus-community/prometheus -n observability -f ./configs/prometheus.yaml

## Añadir métricas del estado del cluster

* helm upgrade prom prometheus-community/prometheus -n observability -f ./configs/prometheus-II-cluster-metrics.yaml


## Verificación de la instalación

* **namespace**: kubectl get namespaces

* **repositorios helm**: helm repo list



