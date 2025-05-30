# FreeBe Meta Backend

## Configuration

### Environment Variables

This application uses environment variables for sensitive configuration. Copy the `.env.example` file to create a `.env` file and fill in your own values:

```bash
cp .env.example .env
```

Then edit the `.env` file with your actual configuration values.

#### Required Environment Variables

- **SMS Configuration**
  - `SMS_SECRET_KEY`: Tencent Cloud SMS secret key
  - `SMS_SECRET_ID`: Tencent Cloud SMS secret ID
  - `SMS_SIGN_NAME`: SMS signature name
  - `SMS_SDK_APP_ID`: SMS SDK application ID

- **OSS Configuration**
  - `OSS_ACCESS_KEY_ID`: Alibaba Cloud OSS access key ID
  - `OSS_ACCESS_KEY_SECRET`: Alibaba Cloud OSS access key secret
  - `OSS_BUCKET_NAME`: OSS bucket name

### Security Best Practices

- Never commit sensitive credentials to Git
- Use environment variables for all secrets
- Keep the `.env` file in your `.gitignore`