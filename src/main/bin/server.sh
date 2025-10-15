#!/bin/bash

# =============================================
# Email MCP Server 启动脚本
# 支持前台（console）和后台（start/stop）模式
# 作者: Your Team
# =============================================

# 应用名
APP_NAME="email-mcp-server"

# 脚本所在目录作为 BASE_DIR
BASE_DIR=$(cd "$(dirname "$0")/.." && pwd)
LIB_DIR="$BASE_DIR/lib"
CONF_DIR="$BASE_DIR/conf"
LOGS_DIR="$BASE_DIR/logs"
PID_FILE="$BASE_DIR/${APP_NAME}.pid"
LOG_FILE="$LOGS_DIR/${APP_NAME}.log"

# JVM 参数
JAVA_OPTS="-Xms512m -Xmx1024m -Dfile.encoding=UTF-8"
# Spring 配置
SPRING_OPTS="--spring.config.location=file:${CONF_DIR}/"

# CLASSPATH：包含所有 JAR
CLASSPATH="$CONF_DIR"
for jar in "$LIB_DIR"/*.jar; do
  CLASSPATH="$CLASSPATH:$jar"
done

# 主类
MAIN_CLASS="hdygxsj.mcp.email.EmailMcpServer"

# 创建日志目录
mkdir -p "$LOGS_DIR"

# 日志函数
log() {
  echo "[$(date +'%Y-%m-%d %H:%M:%S')] $*"
}

# 检查 Java
if ! command -v java &> /dev/null; then
  log "ERROR: Java is not installed or not in PATH."
  exit 1
fi

# 启动函数（前台）
start_foreground() {
  log "Starting $APP_NAME in foreground mode..."
  cd "$BASE_DIR" || exit 1
  exec java $JAVA_OPTS \
       -cp "$CLASSPATH" \
       $MAIN_CLASS \
       $SPRING_OPTS
}

# 启动函数（后台）
start_daemon() {
  if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if kill -0 "$PID" 2>/dev/null; then
      log "ERROR: $APP_NAME is already running (PID: $PID)"
      exit 1
    else
      log "WARN: PID file exists but process not running. Cleaning up..."
      rm -f "$PID_FILE"
    fi
  fi

  log "Starting $APP_NAME in background mode..."
  cd "$BASE_DIR" || exit 1
  nohup java $JAVA_OPTS \
       -cp "$CLASSPATH" \
       $MAIN_CLASS \
       $SPRING_OPTS \
       > "$LOG_FILE" 2>&1 &
  echo $! > "$PID_FILE"
  log "Started $APP_NAME with PID $(cat "$PID_FILE"), log: $LOG_FILE"
}

# 停止函数
stop_daemon() {
  if [ ! -f "$PID_FILE" ]; then
    log "WARN: $APP_NAME is not running (no PID file)"
    exit 1
  fi

  PID=$(cat "$PID_FILE")
  if kill -0 "$PID" 2>/dev/null; then
    log "Stopping $APP_NAME (PID: $PID)..."
    kill "$PID"
    # 等待最多 15 秒
    for i in {1..15}; do
      if ! kill -0 "$PID" 2>/dev/null; then
        break
      fi
      sleep 1
    done
    if kill -0 "$PID" 2>/dev/null; then
      log "WARN: Force killing $APP_NAME..."
      kill -9 "$PID"
    fi
    rm -f "$PID_FILE"
    log "$APP_NAME stopped."
  else
    log "WARN: Process not running. Removing stale PID file."
    rm -f "$PID_FILE"
  fi
}

# 显示帮助
show_help() {
  echo "Usage: $0 {start|stop|run|status}"
  echo "  start    : Start in background (daemon mode)"
  echo "  stop     : Stop background process"
  echo "  run      : Start in foreground (console mode)"
  echo "  status   : Show running status"
}

# 状态检查
show_status() {
  if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if kill -0 "$PID" 2>/dev/null; then
      log "$APP_NAME is running (PID: $PID)"
      exit 0
    else
      log "$APP_NAME is NOT running (stale PID file)"
      exit 1
    fi
  else
    log "$APP_NAME is NOT running"
    exit 1
  fi
}

# 主逻辑
case "$1" in
  start)
    start_daemon
    ;;
  stop)
    stop_daemon
    ;;
  run)
    start_foreground
    ;;
  status)
    show_status
    ;;
  *)
    show_help
    exit 1
    ;;
esac