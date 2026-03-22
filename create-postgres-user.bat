@echo off
echo Création de l'utilisateur PostgreSQL pour AppEvent...
echo.

REM Vérifier si psql est disponible
where psql >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo PostgreSQL n'est pas trouvé dans le PATH.
    echo Veuillez installer PostgreSQL ou ajouter psql au PATH.
    echo.
    echo Si PostgreSQL est installé, essayez de trouver psql.exe et utilisez le chemin complet.
    pause
    exit /b 1
)

echo Connexion à PostgreSQL en tant que superutilisateur...
echo Veuillez entrer le mot de passe de l'utilisateur postgres lorsque demandé.

psql -U postgres -c "CREATE USER iset_events_db WITH PASSWORD 'maram';"
if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la création de l'utilisateur. L'utilisateur existe peut-être déjà.
)

psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE iset_events_db TO iset_events_db;"
if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de l'attribution des privilèges.
)

psql -U postgres -c "ALTER USER iset_events_db CREATEDB;"
if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de l'attribution des droits de création de base de données.
)

echo.
echo Utilisateur iset_events_db créé avec succès!
echo.
pause
