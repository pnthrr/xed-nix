#!/bin/bash
set -e

source "$LOCAL/bin/utils"

MARKER_DIR="$HOME/.lsp/nil"

install() {
  info 'Installing nil language server...'
  mkdir -p "$MARKER_DIR"
  touch "$MARKER_DIR/installed"
  info 'nil installed successfully.'
  exit 0
}

uninstall() {
  info 'Uninstalling nil language server...'
  rm -rf "$MARKER_DIR"
  info 'nil uninstalled successfully.'
  exit 0
}

update() {
  info 'Updating nil language server...'
  touch "$MARKER_DIR/installed"
  info 'nil updated successfully.'
  exit 0
}

case "$1" in
  --uninstall) uninstall;;
  --update) update;;
  *) install;;
esac
