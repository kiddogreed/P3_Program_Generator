#!/bin/bash

# ============================================
#   Setup Cron Job for Sunday Auto-Start
#   Church Program Generator - Pasay 3rd Ward
# ============================================

echo "============================================"
echo "   Cron Job Setup for Sunday Auto-Start"
echo "   Church Program Generator"
echo "============================================"
echo
echo "This script will set up a cron job to automatically"
echo "start the Church Program Generator every Sunday morning."
echo
echo "Cron Schedule:"
echo "  Time: 7:00 AM every Sunday"
echo "  Command: Run sunday-auto-start.sh"
echo

# Get the absolute path to the script
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
STARTUP_SCRIPT="$SCRIPT_DIR/sunday-auto-start.sh"

# Make the startup script executable
chmod +x "$STARTUP_SCRIPT"

# Create the cron entry
CRON_ENTRY="0 7 * * 0 cd $SCRIPT_DIR && bash $STARTUP_SCRIPT >> $SCRIPT_DIR/cron.log 2>&1"

echo "Proposed cron entry:"
echo "$CRON_ENTRY"
echo

read -p "Do you want to add this cron job? (y/n): " -n 1 -r
echo

if [[ $REPLY =~ ^[Yy]$ ]]; then
    # Check if cron entry already exists
    if crontab -l 2>/dev/null | grep -q "sunday-auto-start.sh"; then
        echo "Cron job already exists. Removing old entry..."
        (crontab -l 2>/dev/null | grep -v "sunday-auto-start.sh") | crontab -
    fi
    
    # Add the new cron entry
    (crontab -l 2>/dev/null; echo "$CRON_ENTRY") | crontab -
    
    echo
    echo "============================================"
    echo "   SUCCESS! Cron Job Created"
    echo "============================================"
    echo
    echo "The Church Program Generator will now automatically"
    echo "start every Sunday at 7:00 AM."
    echo
    echo "Current cron jobs:"
    crontab -l
    echo
    echo "To modify the schedule:"
    echo "  crontab -e"
    echo
    echo "To remove the cron job:"
    echo "  crontab -e"
    echo "  (then delete the line with 'sunday-auto-start.sh')"
    echo
    echo "To test manually:"
    echo "  bash $STARTUP_SCRIPT"
    echo
    echo "Logs will be saved to: $SCRIPT_DIR/cron.log"
    echo "============================================"
else
    echo
    echo "Cron job setup cancelled."
    echo
    echo "To set up manually:"
    echo "  1. Run: crontab -e"
    echo "  2. Add this line:"
    echo "     $CRON_ENTRY"
    echo "  3. Save and exit"
    echo
fi

echo
read -p "Press Enter to continue..."