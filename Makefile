start-dev:
	docker-compose -f .\docker\docker-compose.yaml -p "romashkako" up -d

rebuild-dev:
	docker-compose -f .\docker\docker-compose.yaml -p "romashkako" up -d --build