# Makefile

PORT ?= 8085 # Default port, can be overridden

# Target to start the server
start:
	@echo "Starting server on http://localhost:$(PORT)"
	python3 -m http.server $(PORT)

# Target to stop the server (Ctrl+C in terminal is usually enough)
stop:
	@echo "Stopping server (use Ctrl+C in terminal if running)"

# Default target if none specified
default: start

# Clean target (optional, for removing build files if any)
clean:
	@echo "Cleaning up..."
