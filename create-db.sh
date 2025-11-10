#!/bin/bash

# Charger le fichier de configuration s'il existe
if [ -f ".env" ]; then
    echo "Chargement du fichier .env..."
    source .env
fi

# Ordre de priorité : 1) Arguments, 2) Variables d'environnement, 3) Valeurs par défaut
DB_USER=${1:-${DB_USER}}
DB_PASSWORD=${2:-${DB_PASSWORD}}
PROD_DB_NAME=${3:-${PROD_DB_NAME}}
TEST_DB_NAME=${4:-${TEST_DB_NAME}}

echo "Exécution de Data.sql avec les paramètres :"
echo "  - Utilisateur: $DB_USER"
echo "  - Mot de passe: [MASQUÉ]"
echo "  - Base PROD: $PROD_DB_NAME"
echo "  - Base TEST: $TEST_DB_NAME"
echo ""

# Substituer les variables et exécuter
sed -e "s/\${DB_USER}/$DB_USER/g" \
    -e "s/\${DB_PASSWORD}/$DB_PASSWORD/g" \
    -e "s/\${PROD_DB_NAME}/$PROD_DB_NAME/g" \
    -e "s/\${TEST_DB_NAME}/$TEST_DB_NAME/g" \
    resources/Data.sql | mysql -u root -p

echo "Exécution terminée !"